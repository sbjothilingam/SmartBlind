package com.example.project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/*
 * Activity to display the available rules
 * 
 * @author : Suresh Babu Jothilingam
 * @author : Nitish Krishna Ganesan
 * @author : Sarvdeep
 */
public class DisplayRuleActivity extends Activity{


	Button add;
	ListView rules;

	final String piUrl="10.10.10.108:8080";
	static JSONObject ruleList;
	static int edit_CODE = 1, add_CODE = 0;
	static List<String> ruleName;

//Async class to get the rule list from server
	class FetchRules extends AsyncTask<String, Void, String>{
		String response= "";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			response = JSONHandler.sendJSONRequest(params[0], "getRules");
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				ruleList = new JSONObject(result);
				ruleName = new ArrayList<String>();
				for(int i=0;i<ruleList.names().length();i++){
					ruleName.add(ruleList.getJSONObject(ruleList.names().getString(i)).getString("rule"));
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(DisplayRuleActivity.this, android.R.layout.simple_list_item_1, ruleName);
				rules.setAdapter(adapter);
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
	}


//method to initialize the listeners for available widgets

	public void initializeListener(){
		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(DisplayRuleActivity.this, AddRuleActivity.class), add_CODE);
			}
		});


		rules.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				try {
					//Toast.makeText(DisplayRuleActivity.this, "Rule Name "+ruleList.names().getString(position), Toast.LENGTH_SHORT).show();
					startActivityForResult(new Intent(DisplayRuleActivity.this, EditRuleActivity.class).putExtra("ruleName", ruleList.names().getString(position)).putExtra("rule", ruleList.getJSONObject(ruleList.names().getString(position)).getString("rule")), edit_CODE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		/*
		rules.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				try {
					final String ruleName = ruleList.names().getString(position);
					Toast.makeText(DisplayRuleActivity.this, "Rule Name "+ruleName, Toast.LENGTH_SHORT).show();
					AlertDialog.Builder build = new AlertDialog.Builder(DisplayRuleActivity.this)
						.setMessage("Are you Sure that you want to delete "+ruleName+" ?")
						.setTitle("Delete")
						.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								String[] params = new String[2];
								params[0] = piUrl;
								params[1] = ruleName;
								new DeleteRules().execute(params);
								//dialog.cancel();
							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
					AlertDialog dialog = build.create();
					dialog.show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
		});*/
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//ruleBlock = FuzzyLogic.get().getRule();
		setContentView(R.layout.rule_list);
		add = (Button)findViewById(R.id.add_rule);
		rules = (ListView)findViewById(R.id.rule_list);
		initializeListener();
		new FetchRules().execute(piUrl);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == edit_CODE){
			if(resultCode == RESULT_OK){
				new FetchRules().execute(piUrl);
				Toast.makeText(this, "Rule Update", Toast.LENGTH_SHORT).show();
			} else if(resultCode == 3){
				new FetchRules().execute(piUrl);
				Toast.makeText(this, "Rule Deleted", Toast.LENGTH_SHORT).show();
			}
		} else if(requestCode == add_CODE){
			if(resultCode == RESULT_OK){
				new FetchRules().execute(piUrl);
				Toast.makeText(this, "Rule Added", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
