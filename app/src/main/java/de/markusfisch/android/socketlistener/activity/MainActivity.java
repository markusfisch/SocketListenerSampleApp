package de.markusfisch.android.socketlistener.activity;

import de.markusfisch.android.socketlistener.service.SocketListeningService;
import de.markusfisch.android.socketlistener.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (SocketListeningService.MESSAGE_RECEIVED.equals(
					intent.getAction())) {
				textView.append(intent.getStringExtra(
						SocketListeningService.MESSAGE) + "\n");
			}
		}
	};

	private TextView textView;

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.messages);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(
				receiver,
				new IntentFilter(SocketListeningService.MESSAGE_RECEIVED));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}
}
