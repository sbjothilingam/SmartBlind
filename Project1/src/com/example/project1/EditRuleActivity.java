package com.example.project1;

import java.util.HashMap;
import java.util.Map;

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
import android.widget.TextView;
import android.widget.Toast;

/*
 * Activity Class for editing rules
 * 
 * @author : Suresh Babu Jothilingam
 * @author : Nitish Krishna Ganesan
 * @author : Sarvdeep
 */

public class EditRuleActivity extends ActionBarActivity{
	Button update, delete;
	EditText old_rule_name;
	TextView old_rule;
	Spinner term1_old_variable, term2_old_variable, term1_old_negate, term2_old_negate, comb_old, term1_old_result, term2_old_result, cons_old_result, cons_old_negate, cons_old_variable;
	ArrayAdapter<CharSequence> adapter_temperature, adapter_ambient, adapter_variable, adapter_none, adapter_negate;
	final String piUrl="10.10.10.108:8080";

//Async class to update the rule 
	class EditRule extends AsyncTask<String, Void, String>{
		String result = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Map<String, Object> rule = new HashMap<String, Object>();
			rule.put("ruleName", old_rule_name.getText().toString());
			rule.put("term1Variable", term1_old_variable.getSelectedItem().toString());
			rule.put("term1Connection", term1_old_negate.getSelectedItem().toString());
			rule.put("term1Result", term1_old_result.getSelectedItem().toString());
			if(!comb_old.getSelectedItem().toString().equals("NONE")){
				rule.put("connection", comb_old.getSelectedItem().toString());

				rule.put("term2Variable", term2_old_variable.getSelectedItem().toString());
				rule.put("term2Connection", term2_old_negate.getSelectedItem().toString());
				rule.put("term2Result", term2_old_result.getSelectedItem().toString());
			}
			rule.put("conseVariable", cons_old_variable.getSelectedItem().toString());
			rule.put("conseConnection", cons_old_negate.getSelectedItem().toString());
			rule.put("conseResult", cons_old_result.getSelectedItem().toString());

			Log.d("Rule", rule.toString());
			result = JSONHandler.sendJSONRequest(params[0], rule,"editRule");
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("ok")){
				//Toast.makeText(EditRuleActivity.this, result=="OK", Toast.LENGTH_SHORT).show();
				EditRuleActivity.this.setResult(RESULT_OK);
				EditRuleActivity.this.finish();
			} else{
				Toast.makeText(EditRuleActivity.this, result, Toast.LENGTH_SHORT).show();
			}
		}

	}

//Async class to delete a specified rule
	class DeleteRules extends AsyncTask<String, Void, String>{
		String response= "";
		String piUrl = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Map<String, Object> rule = new HashMap<String, Object>();
			rule.put("ruleName", params[1]);
			response = JSONHandler.sendJSONRequest(params[0], rule, "removeRule");
			piUrl = params[0];
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("ok")){
				EditRuleActivity.this.setResult(3);
				EditRuleActivity.this.finish();
			} else{
				Toast.makeText(EditRuleActivity.this, result, Toast.LENGTH_SHORT).show();
			}
		}
	}

//Initialize the spinner with the data
	public void setData(){
		adapter_variable = ArrayAdapter.createFromResource(this, R.array.variable, android.R.layout.simple_spinner_item);
		adapter_variable.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter_negate = ArrayAdapter.createFromResource(this, R.array.negate, android.R.layout.simple_spinner_item);
		adapter_negate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<CharSequence> adapter_cons = ArrayAdapter.createFromResource(this, R.array.blind, android.R.layout.simple_spinner_item);
		adapter_cons.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<CharSequence> adapter_comb = ArrayAdapter.createFromResource(this, R.array.combine, android.R.layout.simple_spinner_item);
		adapter_comb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<CharSequence> adapter_cons_old_variable = ArrayAdapter.createFromResource(this, R.array.cons_variable, android.R.layout.simple_spinner_item);
		adapter_cons_old_variable.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adapter_temperature = ArrayAdapter.createFromResource(this, R.array.temperature, android.R.layout.simple_spinner_item);
		adapter_temperature.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter_ambient = ArrayAdapter.createFromResource(this, R.array.ambient, android.R.layout.simple_spinner_item);
		adapter_ambient.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adapter_none = ArrayAdapter.createFromResource(this, R.array.none, android.R.layout.simple_spinner_item);
		adapter_none.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		term1_old_variable.setAdapter(adapter_variable); term2_old_variable.setAdapter(adapter_none); term1_old_result.setAdapter(adapter_none);
		term1_old_negate.setAdapter(adapter_negate); term2_old_negate.setAdapter(adapter_none); cons_old_negate.setAdapter(adapter_negate);
		comb_old.setAdapter(adapter_comb); term2_old_result.setAdapter(adapter_none);
		cons_old_variable.setAdapter(adapter_cons_old_variable); cons_old_result.setAdapter(adapter_cons); cons_old_negate.setAdapter(adapter_negate);
	}

//method to initialize the listeners for available widgets
	public void initListener(){
		term1_old_variable.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(parent.getItemAtPosition(position).equals("temperature")){
					term1_old_result.setAdapter(adapter_temperature);
				} else if(parent.getItemAtPosition(position).equals("ambient")){
					term1_old_result.setAdapter(adapter_ambient);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		}); 

		comb_old.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(parent.getItemAtPosition(position).equals("NONE")){
					term2_old_negate.setAdapter(adapter_none);
					term2_old_variable.setAdapter(adapter_none);
					term2_old_result.setAdapter(adapter_none);
				} else{
					term2_old_negate.setAdapter(adapter_negate);
					term2_old_variable.setAdapter(adapter_variable);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		term2_old_variable.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(parent.getItemAtPosition(position).equals("temperature")){
					term2_old_result.setAdapter(adapter_temperature);
				} else if(parent.getItemAtPosition(position).equals("ambient")){
					term2_old_result.setAdapter(adapter_ambient);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!old_rule_name.getText().toString().equals("")){
					new EditRule().execute(piUrl);
				} else{
					Toast.makeText(EditRuleActivity.this, "Enter rule name", Toast.LENGTH_SHORT).show();
				}

			}
		});

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DeleteRules().execute(new String[]{piUrl, old_rule_name.getText().toString()});
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_rule);
		update = (Button)findViewById(R.id.update);
		delete = (Button)findViewById(R.id.delete);
		old_rule = (TextView)findViewById(R.id.old_rule);
		term1_old_variable = (Spinner)findViewById(R.id.term1_old_variable);
		term1_old_negate = (Spinner)findViewById(R.id.term1_old_negatiate);
		term1_old_result = (Spinner)findViewById(R.id.term1_old_result);

		term2_old_variable = (Spinner)findViewById(R.id.term2_old_variable);
		term2_old_negate = (Spinner)findViewById(R.id.term2_old_negatiate);
		term2_old_result = (Spinner)findViewById(R.id.term2_old_result);

		comb_old = (Spinner)findViewById(R.id.old_connection);

		cons_old_variable = (Spinner)findViewById(R.id.cons_old_variable);
		cons_old_negate = (Spinner)findViewById(R.id.cons_old_negatiate);
		cons_old_result = (Spinner)findViewById(R.id.cons_old_result);

		old_rule_name = (EditText)findViewById(R.id.old_rule_name);

		old_rule_name.setText(getIntent().getStringExtra("ruleName"));
		old_rule.setText(getIntent().getStringExtra("rule"));
		setData();
		initListener();
	}



}
