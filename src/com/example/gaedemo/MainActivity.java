package com.example.gaedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	public static final String PRODUCT_URI = "http://1-dot-place-it-group-15.appspot.com/product";
	public static final String ITEM_URI = "http://1-dot-place-it-group-15.appspot.com/item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button addItem = (Button) findViewById(R.id.addItem);
		Button addProduct = (Button) findViewById(R.id.addProduct);
		Button showItems = (Button) findViewById(R.id.showItems);
		Button showProducts = (Button) findViewById(R.id.showProducts);
		
		addItem.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this, AddItemActivity.class);
				startActivity(myIntent);
			}
			
		});
		addProduct.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this, AddProductActivity.class);
				startActivity(myIntent);
			}
			
		});
		showProducts.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this, ShowProductsActivity.class);
				startActivity(myIntent);
			}
			
		});
		showItems.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this, ShowItemsActivity.class);
				startActivity(myIntent);
			}
			
		});
	}
}
