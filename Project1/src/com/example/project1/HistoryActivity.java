package com.example.project1;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/*
 * Activity to display the notification history received
 * 
 * @author : Suresh Babu Jothilingam
 * @author : Nitish Krishna Ganesan
 * @author : Sarvdeep
 */

public class HistoryActivity extends ActionBarActivity{
	ListView history_list;

//method to fill the widget with data
	public void fillData(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_list_item_1, MainActivity.history_list);
		history_list.setAdapter(adapter);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_activity);
		history_list = (ListView) findViewById(R.id.history_list);
		fillData();
	}

}
