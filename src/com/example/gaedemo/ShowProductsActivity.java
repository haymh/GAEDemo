package com.example.gaedemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ShowProductsActivity extends Activity {
	protected static final String TAG = "ShowProductsActivity";
	private TextView content;
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showitem);
		content = (TextView)findViewById(R.id.content);
		dialog = ProgressDialog.show(this, "Loading product data...", "Please wait...", false);

		dialog.show();
		//create showproducts layout first.
		// Click on layout, New->Android XML File.
		// ResourceType should be Layout.
		// Add a TextView field in it. 
		//setContentView(R.layout.showproducts);
		
		// Now create a method which get query your Google App Engine via HTTP GET.
		// Make sure that method gets the data via a new thread
		// or use async task.
		// Update the TextView with itemsData.
		
		new GetDataTask().execute();
		
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... arg) {
			final StringBuffer result = new StringBuffer();
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(MainActivity.PRODUCT_URI);	
			try {
				HttpResponse response = client.execute(request);
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				//result.append("Response Code: " + response.getStatusLine().getStatusCode());
				String line = "";
				while((line = rd.readLine()) != null)
					result.append(line);
			} catch (IOException e) {
				Log.d(TAG, "IOException while trying to conect to GAE");
			}
			return result.toString();
		}

		protected void onPostExecute(String result) {
			content.setText(result);
			dialog.dismiss();

		}


	}
}
