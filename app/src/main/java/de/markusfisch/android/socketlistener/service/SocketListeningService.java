package de.markusfisch.android.socketlistener.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketListeningService extends Service {
	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String MESSAGE = "message";
	public static final String MESSAGE_RECEIVED = "message_received";

	private Thread listeningThread;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final String host = intent.getStringExtra(HOST);
		final int port = intent.getIntExtra(PORT, -1);

		if (host == null || host.length() < 1 || port < 0) {
			throw new IllegalArgumentException();
		}

		final OnMessageListener listener = new OnMessageListener() {
			@Override
			public void onMessage(String message) {
				Intent intent = new Intent();
				intent.setAction(MESSAGE_RECEIVED);
				intent.putExtra(MESSAGE, message);
				sendBroadcast(intent);
			}
		};

		(listeningThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (listen(host, port, listener)) {
					// try to re-connect every second
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// ignore and try again
					}
				}
			}
		})).start();

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// we don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy() {
		if (listeningThread != null) {
			for (int retry = 100; retry-- > 0;) {
				try {
					listeningThread.join();
					retry = 0;
				} catch (InterruptedException e) {
					// try again
				}
			}
		}
	}

	private static boolean listen(
			String host,
			int port,
			OnMessageListener listener) {
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(socket.getInputStream(), "UTF-8"));

			for (String message; (message = in.readLine()) != null; ) {
				listener.onMessage(message);
			}

			return true;
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return true;
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// can't do anything about it
				}
			}
		}
	}

	private static interface OnMessageListener {
		void onMessage(String message);
	}
}
