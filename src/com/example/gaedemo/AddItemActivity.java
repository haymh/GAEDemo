package com.example.gaedemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddItemActivity extends Activity {
	public static final String TAG = "AddItemActivity";
	private Spinner spinner;
	private EditText item_name;
	private EditText item_desc;
	private EditText item_price;
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem);
		Button registerItem = (Button) findViewById(R.id.register_item);
		item_name = (EditText) findViewById(R.id.item_name);
		item_desc = (EditText) findViewById(R.id.item_desc);
		item_price = (EditText) findViewById(R.id.item_price);
		spinner = (Spinner) findViewById(R.id.item_spinner);
		
		registerItem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				postdata();
				Intent myIntent = new Intent(AddItemActivity.this, MainActivity.class);
				startActivity(myIntent);
			}
		});
	   
		dialog = ProgressDialog.show(this, "Loading product data...", "Please wait...", false);
	    
		dialog.show();
		new UpdateSpinnerTask().execute(MainActivity.PRODUCT_URI);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void postdata() {
		final ProgressDialog dialog = ProgressDialog.show(this,
				"Posting Data...", "Please wait...", false);
		Thread t = new Thread() {

			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(MainActivity.ITEM_URI);
 
			    try {
			      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			      nameValuePairs.add(new BasicNameValuePair("name",
			    		  item_name.getText().toString()));
			      nameValuePairs.add(new BasicNameValuePair("description",
			    		  item_desc.getText().toString()));
			      nameValuePairs.add(new BasicNameValuePair("price",
			    		  item_price.getText().toString()));
			      nameValuePairs.add(new BasicNameValuePair("product",
			    		  spinner.getSelectedItem().toString()));
			      nameValuePairs.add(new BasicNameValuePair("action",
				          "put"));
			      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			  
			      HttpResponse response = client.execute(post);
			      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			      String line = "";
			      while ((line = rd.readLine()) != null) {
			        Log.d(TAG, line);
			      }

			    } catch (IOException e) {
			    	Log.d(TAG, "IOException while trying to conect to GAE");
			    }
				dialog.dismiss();
			}
		};

		t.start();
		dialog.show();
	}

	 private class UpdateSpinnerTask extends AsyncTask<String, Void, List<String>> {
		 @Override
	     protected List<String> doInBackground(String... url) {
			 
	    	 HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url[0]);
				List<String> list = new ArrayList<String>();
				try {
					HttpResponse response = client.execute(request);
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);
					Log.d(TAG, data);
					JSONObject myjson;
					
					try {
						myjson = new JSONObject(data);
						JSONArray array = myjson.getJSONArray("data");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							list.add(obj.get("name").toString());
						}
						
					} catch (JSONException e) {

				    	Log.d(TAG, "Error in parsing JSON");
					}
					
				} catch (ClientProtocolException e) {

			    	Log.d(TAG, "ClientProtocolException while trying to connect to GAE");
				} catch (IOException e) {

					Log.d(TAG, "IOException while trying to connect to GAE");
				}
	         return list;
	     }

	     protected void onPostExecute(List<String> list) {
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_spinner_item, list);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(dataAdapter);
				dialog.dismiss();
				
	     }

	 }

}
