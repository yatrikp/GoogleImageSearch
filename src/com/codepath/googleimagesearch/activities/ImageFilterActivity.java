package com.codepath.googleimagesearch.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.R.id;
import com.codepath.googleimagesearch.R.layout;
import com.codepath.googleimagesearch.R.menu;
import com.codepath.googleimagesearch.models.ImageFilter;

public class ImageFilterActivity extends Activity {
	
	private ImageFilter filter;
	private Spinner sizeSpinner;
	private Spinner colorSpinner;
	private Spinner typeSpinner;
	private EditText siteText;
	private TextView done;
	private TextView cancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_filter);
		sizeSpinner = (Spinner) findViewById(R.id.spinnerSize);
		colorSpinner = (Spinner) findViewById(R.id.spinnerColor);
		typeSpinner = (Spinner) findViewById(R.id.spinnerType);
		siteText = (EditText) findViewById(R.id.etSite);
		done = (TextView) findViewById(R.id.tvDone);
		cancel = (TextView) findViewById(R.id.tvCancel);
		
		filter = (ImageFilter) getIntent().getSerializableExtra(ImageSearchActivity.FILTER_KEY);
		initializeControls();
		initializeButtons();
	}
	
	private void initializeControls() {
		List<String> types = new ArrayList<String>();
		types.add("any");
		types.add("face");
		types.add("photo");
		types.add("clipart");
		types.add("lineart");
		
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(typeAdapter);
		
		String type = filter.getType();
		if(type == null || type.equals("") || type.equals("any")){
			typeSpinner.setSelection(0);
		}else if(type.equals("face")){
			typeSpinner.setSelection(1);
		}else if(type.equals("photo")){
			typeSpinner.setSelection(2);
		}else if(type.equals("clipart")){
			typeSpinner.setSelection(3);
		}else if(type.equals("lineart")){
			typeSpinner.setSelection(4);
		}

		// color spinner
		List<String> colors = new ArrayList<String>();
		colors.add("any");
		colors.add("black");
		colors.add("blue");
		colors.add("brown");
		colors.add("gray");
		colors.add("orange");
		colors.add("green");
		colors.add("pink");
		colors.add("red");
		colors.add("white");
		colors.add("yellow");
		colors.add("purple");
				
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors);
		colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		colorSpinner.setAdapter(colorAdapter);
		
		String color = filter.getColor();
		if(color == null || color.equals("") || color.equals("any")){
			colorSpinner.setSelection(0);
		}else if(color.equals("black")){
			colorSpinner.setSelection(1);
		}else if(color.equals("blue")){
			colorSpinner.setSelection(2);
		}else if(color.equals("brown")){
			colorSpinner.setSelection(3);
		}else if(color.equals("gray")){
			colorSpinner.setSelection(4);
		}else if(color.equals("orange")){
			colorSpinner.setSelection(5);
		}else if(color.equals("green")){
			colorSpinner.setSelection(6);
		}else if(color.equals("pink")){
			colorSpinner.setSelection(7);
		}else if(color.equals("red")){
			colorSpinner.setSelection(8);
		}else if(color.equals("white")){
			colorSpinner.setSelection(9);
		}else if(color.equals("yellow")){
			colorSpinner.setSelection(10);
		}else if(color.equals("purple")){
			colorSpinner.setSelection(11);
		}
		
		// size spinner
		List<String> sizes = new ArrayList<String>();
		sizes.add("any");
		sizes.add("icon");
		sizes.add("small");
		sizes.add("medium");
		sizes.add("large");
		sizes.add("xlarge");
		sizes.add("xxlarge");
		sizes.add("huge");
		
		ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sizes);
		sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sizeSpinner.setAdapter(sizeAdapter);
		
		String size = filter.getSize();
		if(size == null || size.equals("") || size.equals("any")){
			sizeSpinner.setSelection(0);
		}else if(size.equals("icon")){
			sizeSpinner.setSelection(1);
		}else if(size.equals("small")){
			sizeSpinner.setSelection(2);
		}else if(size.equals("medium")){
			sizeSpinner.setSelection(3);
		}else if(size.equals("large")){
			sizeSpinner.setSelection(4);
		}else if(size.equals("xlarge")){
			sizeSpinner.setSelection(5);
		}else if(size.equals("xxlarge")){
			sizeSpinner.setSelection(6);
		}else if(size.equals("huge")){
			sizeSpinner.setSelection(7);
		}

		// site
		siteText.setText(filter.getSite());
		siteText.setSelection(siteText.getText().length());	
	}
	
	private void initializeButtons() {
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setFiltersAndFinishActivity();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	protected void setFiltersAndFinishActivity() {
		String size = sizeSpinner.getSelectedItem().toString();
		String color = colorSpinner.getSelectedItem().toString();
		String type = typeSpinner.getSelectedItem().toString();
		String s = siteText.getText().toString();
		
		ImageFilter filter = new ImageFilter(size, color, type, s);
		
		Intent data = new Intent();
		data.putExtra(ImageSearchActivity.FILTER_KEY, filter);
		setResult(RESULT_OK, data);
		
		// end this activity to return to the parent activity
		finish();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.image_filter, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	setResult(RESULT_OK);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
