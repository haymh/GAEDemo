package com.example.gaedemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShowProductsActivity extends ListActivity {
	protected static final String TAG = "ShowProductsActivity";
	ProgressDialog dialog;
	
	private ArrayList<JSONDetail> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_list);
		
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
			list = new ArrayList<JSONDetail>(new JSONParser(result).getDetailsArray());
			setAdapter(list);
			
			dialog.dismiss();
		}
	}
	
	private void setAdapter(ArrayList<JSONDetail> list) {
		setListAdapter(new Adapter(ShowProductsActivity.this, R.layout.display_object, list));
	}
		
	public class Adapter extends ArrayAdapter<JSONDetail> {
		
		public Adapter(Context context, int textResource, List<JSONDetail> objects) {
			super(context, textResource, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			
			if(v == null) {
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.display_object, null);
			}

			JSONDetail item = getItem(position);
			
			if(item != null) {
				
				TextView name = (TextView) v.findViewById(R.id.objectName);
				TextView description = (TextView) v.findViewById(R.id.objectDescription);
				TextView product = (TextView) v.findViewById(R.id.objectProduct);
				TextView price = (TextView) v.findViewById(R.id.objectPrice);
				
				name.setText(item.getName());
				description.setText("DESCRIPTION: " + item.getDescription());
				product.setVisibility(View.GONE);
				price.setVisibility(View.GONE);
			}
			return v;
		}
	}
}
