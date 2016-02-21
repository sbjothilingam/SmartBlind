package com.example.project1;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/*
 * Activity to add a new rule
 * 
 * @author : Suresh Babu Jothilingam
 * @author : Nitish Krishna Ganesan
 * @author : Sarvdeep
 */
public class AddRuleActivity extends ActionBarActivity{
	Button add;
	EditText new_rule_name;
	Spinner term1_variable, term2_variable, term1_negate, term2_negate, comb, term1_result, term2_result, cons_result, cons_negate, cons_variable;
	ArrayAdapter<CharSequence> adapter_temperature, adapter_ambient, adapter_variable, adapter_none, adapter_negate;
	final String piUrl="10.10.10.108:8080";

//Async class to send the new rule to add to the server
	class AddRule extends AsyncTask<String, Void, String>{
		String result = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Map<String, Object> rule = new HashMap<String, Object>();
			rule.put("ruleName", new_rule_name.getText().toString());
			rule.put("term1Variable", term1_variable.getSelectedItem().toString());
			rule.put("term1Connection", term1_negate.getSelectedItem().toString());
			rule.put("term1Result", term1_result.getSelectedItem().toString());
			if(!comb.getSelectedItem().toString().equals("NONE")){
				rule.put("connection", comb.getSelectedItem().toString());

				rule.put("term2Variable", term2_variable.getSelectedItem().toString());
				rule.put("term2Connection", term2_negate.getSelectedItem().toString());
				rule.put("term2Result", term2_result.getSelectedItem().toString());
			}
			rule.put("conseVariable", cons_variable.getSelectedItem().toString());
			rule.put("conseConnection", cons_negate.getSelectedItem().toString());
			rule.put("conseResult", cons_result.getSelectedItem().toString());

			Log.d("Rule", rule.toString());
			result = JSONHandler.sendJSONRequest(params[0], rule,"addRule");
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("ok")){
				//Toast.makeText(EditRuleActivity.this, result=="OK", Toast.LENGTH_SHORT).show();
				AddRuleActivity.this.setResult(RESULT_OK);
				AddRuleActivity.this.finish();
			} else{
				Toast.makeText(AddRuleActivity.this, result, Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	//method to initialize the spinners with data
	public void setData(){
		adapter_variable = ArrayAdapter.createFromResource(this, R.array.variable, android.R.layout.simple_spinner_item);
		adapter_variable.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter_negate = ArrayAdapter.createFromResource(this, R.array.negate, android.R.layout.simple_spinner_item);
		adapter_negate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<CharSequence> adapter_cons = ArrayAdapter.createFromResource(this, R.array.blind, android.R.layout.simple_spinner_item);
		adapter_cons.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<CharSequence> adapter_comb = ArrayAdapter.createFromResource(this, R.array.combine, android.R.layout.simple_spinner_item);
		adapter_comb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<CharSequence> adapter_cons_variable = ArrayAdapter.createFromResource(this, R.array.cons_variable, android.R.layout.simple_spinner_item);
		adapter_cons_variable.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adapter_temperature = ArrayAdapter.createFromResource(this, R.array.temperature, android.R.layout.simple_spinner_item);
		adapter_temperature.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter_ambient = ArrayAdapter.createFromResource(this, R.array.ambient, android.R.layout.simple_spinner_item);
		adapter_ambient.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adapter_none = ArrayAdapter.createFromResource(this, R.array.none, android.R.layout.simple_spinner_item);
		adapter_none.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		term1_variable.setAdapter(adapter_variable); term2_variable.setAdapter(adapter_none); term1_result.setAdapter(adapter_none);
		term1_negate.setAdapter(adapter_negate); term2_negate.setAdapter(adapter_none); cons_negate.setAdapter(adapter_negate);
		comb.setAdapter(adapter_comb); term2_result.setAdapter(adapter_none);
		cons_variable.setAdapter(adapter_cons_variable); cons_result.setAdapter(adapter_cons); cons_negate.setAdapter(adapter_negate);
	}

//method to initialize the listeners for available widgets

	public void initListener(){
		term1_variable.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(parent.getItemAtPosition(position).equals("temperature")){
					term1_result.setAdapter(adapter_temperature);
				} else if(parent.getItemAtPosition(position).equals("ambient")){
					term1_result.setAdapter(adapter_ambient);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		}); 

		comb.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(parent.getItemAtPosition(position).equals("NONE")){
					term2_negate.setAdapter(adapter_none);
					term2_variable.setAdapter(adapter_none);
					term2_result.setAdapter(adapter_none);
				} else{
					term2_negate.setAdapter(adapter_negate);
					term2_variable.setAdapter(adapter_variable);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		term2_variable.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(parent.getItemAtPosition(position).equals("temperature")){
					term2_result.setAdapter(adapter_temperature);
				} else if(parent.getItemAtPosition(position).equals("ambient")){
					term2_result.setAdapter(adapter_ambient);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!new_rule_name.getText().toString().equals("")){
					new AddRule().execute(piUrl);
				} else{
					Toast.makeText(AddRuleActivity.this, "Enter rule name", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_rule);
		add = (Button)findViewById(R.id.add);
		term1_variable = (Spinner)findViewById(R.id.term1_variable);
		term1_negate = (Spinner)findViewById(R.id.term1_negatiate);
		term1_result = (Spinner)findViewById(R.id.term1_result);

		term2_variable = (Spinner)findViewById(R.id.term2_variable);
		term2_negate = (Spinner)findViewById(R.id.term2_negatiate);
		term2_result = (Spinner)findViewById(R.id.term2_result);

		comb = (Spinner)findViewById(R.id.connection);

		cons_variable = (Spinner)findViewById(R.id.cons_variable);
		cons_negate = (Spinner)findViewById(R.id.cons_negatiate);
		cons_result = (Spinner)findViewById(R.id.cons_result);

		new_rule_name = (EditText)findViewById(R.id.new_rule_name);

		setData();
		initListener();
	}




}
