# Ktor Client CIO net address reconnect bug demo

Hello there, this little demo code probably is a bit more complext then it needs to be. Sorry.

Here are the steps of the demo:

1) Run the main function in KtorClientReconnectDemo.kt to see that we connect to a websocket happily.
2) Stop the process
3) Disconnect your network
4) Run the main function again. It will error and try to reconnect.
5) Connect your network again. The errors carry on coming.

