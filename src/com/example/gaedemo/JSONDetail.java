package com.example.gaedemo;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;



public class JSONDetail {
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String PRODUCT = "product";
	private static final String PRICE = "price";
	
	private static final String TAG = JSONDetail.class.getSimpleName();
	
	private String name;
	private String description;
	private String product;
	private String price;
	
	public JSONDetail(JSONObject object) {
		try {
			name = object.getString(NAME);
		} catch (JSONException e) {}

		try {
			description = object.getString(DESCRIPTION);
		} catch (JSONException e) {}

		try {
			product = object.getString(PRODUCT);
		} catch (JSONException e) {}

		try {
			price = object.getString(PRICE);
		} catch (JSONException e) {}

	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getProduct() {
		return product;
	}
	
	public String getPrice() {
		return price;
	}
}
