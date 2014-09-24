package com.codepath.googleimagesearch.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.adapters.ImageResultAdapter;
import com.codepath.googleimagesearch.models.ImageFilter;
import com.codepath.googleimagesearch.models.ImageResult;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ImageSearchActivity extends Activity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener {
	
	private StaggeredGridView gvResults;
	public ArrayList<ImageResult> imageResults;
	private ImageResultAdapter aImageResults;
	private Context context;
	private String queryVal = "android";
	private SearchView searchView;
	private ImageFilter imageFilter;
	private ProgressBar progressBar;
	
	private int scrolledPageNo = 0;
	
	public static final String COLOR_KEY = "imgcolor";
	public static final String SIZE_KEY = "imgsz";
	public static final String TYPE_KEY = "imgtype";
	public static final String SITE_KEY = "as_sitesearch";
	
	private int FILTER_REQUEST_CODE = 26;
	public static String FILTER_KEY = "result";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_search);
		context = this;
		imageFilter = new ImageFilter();
		// creates the data source		
		imageResults = new ArrayList<ImageResult>();
		setupViews();
		
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			queryVal = query;
			prepareCall();
		}
		
	}
	
	// Should be called manually when an async task has started
    public void showProgressBar() {
//        setProgressBarIndeterminateVisibility(true); 
        progressBar.setVisibility(View.VISIBLE);
    }

    // Should be called when an async task has finished
    public void hideProgressBar() {
//        setProgressBarIndeterminateVisibility(false);
    	progressBar.setVisibility(View.INVISIBLE);
    }

	private void setupViews() {
		gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
		
		// attach the datasource to the adapter
		aImageResults = new ImageResultAdapter(this, imageResults);
		// link the adapter to the adapterview
		gvResults.setAdapter(aImageResults);
		gvResults.setOnScrollListener(this);
		gvResults.setOnItemClickListener(this);
	
		// OnItemLongClickListener
		gvResults.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
	        public boolean onItemLongClick(AdapterView<?> parent, View v, int position, 
	        		long id) {
				// share the image
//	        	onShareItem(v);
	        	return true;
	        }
	    });

		ViewGroup layout = (ViewGroup) findViewById(android.R.id.content).getRootView();

		progressBar = new ProgressBar(this,null,android.R.attr.progressBarStyleLarge);
		progressBar.setIndeterminate(true);
		hideProgressBar();
		RelativeLayout.LayoutParams params = new 
		        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
		RelativeLayout rl = new RelativeLayout(this);
		rl.setGravity(Gravity.CENTER);
		rl.addView(progressBar);
		layout.addView(rl,params);
		
//		makeNetworkCall(queryVal, scrolledPageNo);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_filter:
	        	showImageFilter();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		prepareCall();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.image_search, menu);
		
		MenuItem searchItem = menu.findItem(R.id.action_search);
	    searchView = (SearchView) searchItem.getActionView();
	    
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		try{
	    	
	    	searchView.setOnQueryTextListener(new OnQueryTextListener() {
			       @Override
			       public boolean onQueryTextSubmit(String query) {
			    	   queryVal = query;
			    	   hideSoftKeyboard(searchView);
			    	   prepareCall();			    	   
			           return true;
			       }
				
			       @Override
			       public boolean onQueryTextChange(String newText) {
			           return false;
			       }
			   });
	    	
	    }catch(Exception ex){
	    	System.out.println(ex.getStackTrace());
	    }
	   
		return true;
	}
	
	// dsiplays the Image Filter with advanced filter options
	private void showImageFilter() {
		Intent i = new Intent(ImageSearchActivity.this, ImageFilterActivity.class);
		i.putExtra(FILTER_KEY, imageFilter);
		startActivityForResult(i, FILTER_REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == FILTER_REQUEST_CODE){
			if(resultCode == RESULT_OK){
				imageFilter = (ImageFilter) data.getSerializableExtra("result");
				prepareCall();
			}
		}
		
	}
	
	private RequestParams getRequestParams(){
		RequestParams params = new RequestParams();
		
		if(!isEmpty(imageFilter.getColor())){
			params.put(COLOR_KEY, imageFilter.getColor());
		}
		if(!isEmpty(imageFilter.getSize())){
			params.put(SIZE_KEY, imageFilter.getSize());
		}
		if(!isEmpty(imageFilter.getType())){
			params.put(TYPE_KEY, imageFilter.getType());
		}
		if(!isEmpty(imageFilter.getSite())){
			params.put(SITE_KEY, imageFilter.getSite());
		}
		
		return params;
	}
	
	private boolean isEmpty(String str){
		if(str == null || str.trim().length() == 0 || str.equals("") || str.equals("any")){
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		Intent i = new Intent(ImageSearchActivity.this, ImageDisplayActivity.class);
//		// get the image result to display
		ImageResult imageResult = imageResults.get(position);
//		// pass image result into the intent
		i.putExtra(FILTER_KEY, imageResult);
//		// launch the activity
		startActivity(i);
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		Log.d("Activity", "onScrollStateChanged:" + scrollState);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (((firstVisibleItem + visibleItemCount) >= totalItemCount) && 
			scrolledPageNo<=8 && totalItemCount > 0){
			scrolledPageNo++;
			makeNetworkCall(queryVal, scrolledPageNo);						
		}
	}
		
	private void prepareCall(){
		getWindow().getDecorView().setBackgroundColor(color.background_dark);
		showProgressBar();
		clearAdapter();
		scrolledPageNo = 0; 	   	
 	   	if(!isEmpty(queryVal)){
 	 	   	makeNetworkCall(queryVal, 0);
 	   	}
 	   	hideProgressBar();
	}
	
	private void clearAdapter() {
		aImageResults.clear();
		aImageResults.notifyDataSetInvalidated();		
	}

	
	private void makeNetworkCall(String query, int page){
		String q = "q="+Uri.encode(query)+"&rsz=8&start="+ ((page*7) + 1);
		AsyncHttpClient client = new AsyncHttpClient();
		if (!checkIfNetworkAvailable()) {
			Toast.makeText(this, "Network not available. Try again later", Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParams requestParams = getRequestParams();
		String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&" + q;
		client.get(searchUrl, requestParams, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Log.d("debug", response.toString());
				JSONArray imageResultsJson = null;
				try{
					imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
					if (imageResultsJson != null){
						aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
						aImageResults.notifyDataSetChanged();
					}
				}
				catch(JSONException e){
					e.printStackTrace();
				}
			}
				
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// Handle the failure and alert the user to retry
			   Toast.makeText(context, "Network failure : "+ throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();				  
			}
		});
	}
	
	private void hideSoftKeyboard(View view) {
		InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		inputMethodMgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	private Boolean checkIfNetworkAvailable() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = connManager.getActiveNetworkInfo();
        return nwInfo != null && nwInfo.isConnectedOrConnecting();
    }
}
