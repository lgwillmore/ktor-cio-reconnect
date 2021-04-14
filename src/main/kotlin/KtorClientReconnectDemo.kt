import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

private val gson = Gson()

suspend fun CoroutineScope.connectAndConsumeFromAWebsocket(
    client: HttpClient,
    errorHandler: CoroutineErrorHandler
): ReceiveChannel<String> {
    val channel = Channel<String>(5)
    launch(CoroutineExceptionHandler(errorHandler)) {
        client.wss(
            method = HttpMethod.Get,
            host = "api.valr.com",
            path = "/ws/trade"
        ) {
            outgoing.send(
                Frame.Text(
                    gson.toJson(
                        ValrSubscriptionRequest(
                            subscriptions = listOf(
                                ValrSubscription.AggregatedOrderBook(
                                    pairs = listOf("BTCZAR")
                                )
                            )
                        )
                    )
                )
            )
            for (message in incoming) {
                when (message) {
                    is Frame.Text -> {
                        channel.send(message.readText())
                    }
                    is Frame.Ping -> {
                        send(Frame.Pong(message.buffer))
                    }
                    is Frame.Pong -> {
                        println("Pong")
                    }
                }
            }
        }
    }
    return channel
}

fun main() {
    val client = HttpClient(CIO) {
        expectSuccess = false
        install(WebSockets)
        engine {
            requestTimeout = 10_000
        }
    }

    runBlocking {
        val supervisor = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.Unconfined + supervisor)
        while (true) {
            scope.launch {
                try {
                    println("connecting")
                    val channel = connectAndConsumeFromAWebsocket(client) { context, throwable ->
                        println("Coroutine Error")
                    }
                    for (message in channel) {
                        println(message)
                    }
                } catch (e: Exception) {
                    println("Got an error, will try to reconnect in a bit.")
                }
            }.join()
            delay(5000)
        }
    }
}