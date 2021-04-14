import kotlin.coroutines.CoroutineContext

data class ValrSubscriptionRequest(
    val subscriptions: List<ValrSubscription>
) {
    val type = "SUBSCRIBE"
}

sealed class ValrSubscription {
    abstract val event: String

    data class AggregatedOrderBook(
        val pairs: List<String>
    ) : ValrSubscription() {
        override val event = "AGGREGATED_ORDERBOOK_UPDATE"
    }

    data class NewTrade(
        val pairs: List<String>
    ) : ValrSubscription() {
        override val event = "NEW_TRADE"
    }
}

typealias CoroutineErrorHandler = (context: CoroutineContext, throwable: Throwable) -> Unit