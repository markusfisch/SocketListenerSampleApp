package de.markusfisch.android.socketlistener.app;

import de.markusfisch.android.socketlistener.service.SocketListeningService;

import android.app.Application;
import android.content.Intent;

public class SocketListenerApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		Intent intent = new Intent(this, SocketListeningService.class);
		intent.putExtra(SocketListeningService.HOST, "10.200.1.239");
		intent.putExtra(SocketListeningService.PORT, 7575);
		startService(intent);
	}
}
