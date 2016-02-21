package com.example.project1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * MainActivity which gets displayed when the Application Launches
 * 
 * @author : Suresh Babu Jothilingam
 * @author : Nitish Krishna Ganesan
 * @author : Sarvdeep
 */

public class MainActivity extends ActionBarActivity {

	static ArrayList<String> history_list;

	TextView indoor_temp_text, indoor_ambient_text, indoor_blind_status, time_updated;
	Button rule, history, update_setting;
	final String piUrl="10.10.10.108:8080";
	UpdateReciever receiver;

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		registerReceiver(receiver, new IntentFilter("com.example.project1.tempUpdate"));
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(receiver);
	}

//Class to get the current state of the room 
	class FetchRoomStaus extends AsyncTask<String, Void, String>{
		String response = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			response = JSONHandler.sendJSONRequest(params[0], "getRoomStatus");
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				JSONObject resp = new JSONObject(result);
				indoor_temp_text.setText(resp.get("temperature").toString()+" F");
				indoor_ambient_text.setText(resp.get("ambient").toString().toUpperCase());
				indoor_blind_status.setText(resp.get("blind").toString().toUpperCase());
				time_updated.setText("Last update : "+resp.get("time").toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class UpdateStatus extends AsyncTask<String, Void, Void>{
		String response = "";

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			JSONHandler.sendJSONRequest(params[0], "updateStatus");
			return null;
		}

	}

//method to initialize the listeners for available widgets

	public void initListener(){
		rule.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, DisplayRuleActivity.class));
			}
		});

		history.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, HistoryActivity.class));
			}
		});

		update_setting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, SettingsActivity.class));

			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		history_list = new ArrayList<String>();
		receiver = new UpdateReciever();

		indoor_ambient_text = (TextView)findViewById(R.id.ambient_text_indoor);
		indoor_blind_status = (TextView)findViewById(R.id.blind_text_indoor);
		indoor_temp_text = (TextView)findViewById(R.id.temp_text_indoor);
		time_updated = (TextView)findViewById(R.id.time_updated);
		rule = (Button)findViewById(R.id.rule);
		history = (Button) findViewById(R.id.history);
		update_setting = (Button) findViewById(R.id.set_difference);

		if(!RoomStatusService.isServiceRunning())
			startService(new Intent(this, RoomStatusService.class));

		new FetchRoomStaus().execute(piUrl);
		new UpdateStatus().execute(piUrl);
		
		//new Fetch().execute(weather_url);

		registerReceiver(receiver, new IntentFilter("com.example.project1.tempUpdate"));

		initListener();
	}

//Broadcast receiver class to receive the notification
	class UpdateReciever extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//Toast.makeText(MainActivity.this, "Update received "+intent.getStringExtra("Test"), Toast.LENGTH_SHORT).show();
			indoor_temp_text.setText(intent.getStringExtra("temperature")+" F");
			indoor_ambient_text.setText(intent.getStringExtra("ambient").toUpperCase());
			indoor_blind_status.setText(intent.getStringExtra("blind").toUpperCase());
			time_updated.setText("Last update : "+intent.getStringExtra("time"));
			history_list.add(intent.getStringExtra("time")+", "+intent.getStringExtra("temperature"));
			NotificationCompat.Builder build = new NotificationCompat.Builder(context)
			.setSmallIcon(R.drawable.abc_ab_share_pack_holo_light)
			.setContentTitle("Room Temperature Update")
			.setContentText(intent.getStringExtra("time")+", "+intent.getStringExtra("temperature"))
			.setAutoCancel(true);

			NotificationManager notify = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
			notify.notify(0, build.build());
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
