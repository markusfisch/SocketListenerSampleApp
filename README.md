# Socket Listener Sample App

Open a socket to some server and try to reconnect if the socket has closed.

Read a line from that socket and show it in the app.

Can be used as a simple push message replacement in a local WiFi.

*This is a demo!*

A real world app should start/stop the listening service in response
to WiFi connectivity to save battery.

## How To Use

* Insert the address of your server in app/SocketListenerApp.java:

```java
Intent intent = new Intent(this, SocketListeningService.class);
intent.putExtra(SocketListeningService.HOST, "10.200.1.239");
intent.putExtra(SocketListeningService.PORT, 7575);
startService(intent);
```

* Open a socket on that machine and listen on it:

```sh
$ netcat -l 7575
```

`netcat` will wait for you to write something.

* Run the app
* Write something after the `netcat` command and hit Enter
