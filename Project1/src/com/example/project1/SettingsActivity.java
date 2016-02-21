package com.example.project1;

import java.util.HashMap;
import java.util.Map;

import android.content.AsyncQueryHandler;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/*
 * Notification Activity to set user notification preference
 * 
 * @author : Suresh Babu Jothilingam
 * @author : Nitish Krishna Ganesan
 * @author : Sarvdeep
 */

public class SettingsActivity extends ActionBarActivity{
	ImageButton plus, minus;
	EditText value;
	Button set;
	static String piUrl = "10.10.10.108:8080";
	
	//async class to send request to server to update the notification preference
	class UpdateTempSetting extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("temperature", value.getText().toString());
			JSONHandler.sendJSONRequest(params[0], map, "updateNotification");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
		}

	}
	
	//method to initialize the listeners for available widgets
	public void initListener(){
		plus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				value.setText(String.valueOf(Integer.parseInt(value.getText().toString())+1));
			}
		});
		minus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Integer.parseInt(value.getText().toString())>1){
					value.setText(String.valueOf(Integer.parseInt(value.getText().toString())-1));
				}
			}
		});

		set.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new UpdateTempSetting().execute(piUrl);
			}
		});
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		plus = (ImageButton) findViewById(R.id.plus);
		minus = (ImageButton) findViewById(R.id.minus);
		value = (EditText) findViewById(R.id.setting_value);
		set = (Button) findViewById(R.id.settings_temp);
		initListener();
	}

}
