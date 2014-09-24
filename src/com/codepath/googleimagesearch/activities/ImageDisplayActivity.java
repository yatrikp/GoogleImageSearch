package com.codepath.googleimagesearch.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.adapters.TouchImageView;
import com.codepath.googleimagesearch.models.ImageResult;
import com.codepath.googleimagesearch.utils.Util;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends Activity {
	
//	private ProgressBar spinner;
	private MenuItem save;
	private MenuItem share;
	private ShareActionProvider mShareAction;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_image_display);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		// pull the out the url for image to display
		ImageResult imageResult = (ImageResult) getIntent().getSerializableExtra(ImageSearchActivity.FILTER_KEY);
		// set the title 
		this.getActionBar().setTitle(Html.fromHtml(imageResult.title));		
		try {
            int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
            if (titleId > 0){
                TextView titleView = (TextView)findViewById(titleId);
                titleView.setTextSize(10);
            }
        } catch (Exception e) {
            Log.e("", "Failed to obtain action bar title reference");
        }
		
		
		
		// find the image view
		TouchImageView imageView = (TouchImageView)findViewById(R.id.ivImageResult);
		showProgressBar();
		// load the image using picassa lib
		Picasso.with(this).load(imageResult.url).into(imageView);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				setupShareAction();
				
			}
		};
		handler.postDelayed(runnable, 3000);
		hideProgressBar();
	}
	
	// Should be called manually when an async task has started
    public void showProgressBar() {
        setProgressBarIndeterminateVisibility(true); 
    }

    // Should be called when an async task has finished
    public void hideProgressBar() {
        setProgressBarIndeterminateVisibility(false); 
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.image_display, menu);
		share = menu.findItem(R.id.menu_item_share);
		save = menu.findItem(R.id.menu_item_save);
		share.setEnabled(false);
		save.setEnabled(false);
		mShareAction = (ShareActionProvider) share.getActionProvider();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				setResult(RESULT_OK);
				this.finish();
				return true;
	
			case R.id.menu_item_save:
				saveImage();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setupShareAction() {
		TouchImageView ivImage = (TouchImageView) findViewById(R.id.ivImageResult);
		Uri bmpUri = getUri(ivImage); // see previous section
		if (bmpUri != null) {
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
			shareIntent.setType("image/*");
			mShareAction.setShareIntent(shareIntent);
			share.setEnabled(true);
			save.setEnabled(true);
		} else {
			Toast.makeText(this, "Cannot share this image", Toast.LENGTH_SHORT).show();			
		}
	}
		
	public Uri getUri(ImageView imageView) {
		Bitmap bitmap = null;
		Drawable drawable = imageView.getDrawable();
		if (drawable instanceof BitmapDrawable) {
			bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		} else {
			return null;
		}
		
		Uri bmpUri = null;
		try {
			String fileName = "image_search_" + System.currentTimeMillis() + ".png";
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
					fileName);
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
			bmpUri = Uri.fromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmpUri;
	}
	
	private void saveImage() {
		Uri uri = null;
		OutputStream outputStream = null;
		TouchImageView ivImageResult = (TouchImageView) findViewById(R.id.ivImageResult);
		ivImageResult.buildDrawingCache();
		Bitmap bm = ivImageResult.getDrawingCache();
		
		try {
			File dir = new File(this.getFilesDir(),
					System.currentTimeMillis() + ".png");
			uri = Uri.fromFile(dir);
			outputStream = new FileOutputStream(dir);
		} catch (Exception e) {
			Toast.makeText(this, "Error occured",
					Toast.LENGTH_SHORT).show();
		}
		try {
			bm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			Toast.makeText(this,
					"Error occured", Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(this, "Image saved to Downloads", Toast.LENGTH_SHORT)
				.show();
	}

}
