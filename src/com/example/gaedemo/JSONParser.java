package com.example.gaedemo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	private static final String TAG = JSONParser.class.getSimpleName();
	
	private static final String HEADER = "data";

	private JSONArray array;
	private JSONObject object;
	
	public JSONParser(String string) {
		setString(string);
	}
	
	public void setString(String string) {
		try {
			object = new JSONObject(string);
			parse();
		} catch (JSONException e) {
			Log.wtf(TAG, "setString() " + e.toString());
		}
	}
	
	private void parse() {
		try {
			array = new JSONArray(object.getString(HEADER));
		} catch (JSONException e) {
			Log.wtf(TAG, "parse() " + e.toString());
		}
	}
	
	public ArrayList<JSONDetail> getDetailsArray() {
		ArrayList<JSONDetail> list = new ArrayList<JSONDetail>();
		
		for(int i = 0; i < array.length(); i++) {
			
			try {
				JSONObject single = array.getJSONObject(i);
						
				list.add( new JSONDetail(single) );
			}catch(JSONException e) {
				Log.wtf(TAG, "getDetailsArray() " + e.toString());
			}
		}
		
		return list;
	}
}
