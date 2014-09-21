package com.codepath.googleimagesearch.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.models.ImageResult;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

public class ImageResultAdapter extends ArrayAdapter<ImageResult> {
	
	private final LayoutInflater mLayoutInflater;
	private final Random mRandom;
	private final ArrayList<Integer> mBackgroundColors;
	private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
	public ImageLoader imageLoader = ImageLoader.getInstance();
	
	public static class ViewHolder {
		DynamicHeightImageView ivImage;
//        TextView tvTitle;        
    }

	public ImageResultAdapter(Context context, List<ImageResult> objects) {
		super(context, android.R.layout.simple_list_item_1, objects);
		this.mLayoutInflater = LayoutInflater.from(context);
		this.mRandom = new Random();
		mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(android.R.color.holo_orange_light);
        mBackgroundColors.add(android.R.color.holo_green_dark);
        mBackgroundColors.add(android.R.color.holo_blue_bright);
        mBackgroundColors.add(android.R.color.holo_red_dark);
        mBackgroundColors.add(android.R.color.darker_gray);
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
		 ImageResult imageInfo = getItem(position);
		 if (convertView == null){
			 convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
			 holder = new ViewHolder();
			 holder.ivImage = (DynamicHeightImageView)convertView.findViewById(R.id.ivImage);
//			 holder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
			 convertView.setTag(holder);
		 }
		 else{
             holder = (ViewHolder) convertView.getTag();
	     }
		 
		 try{
//			 holder.tvTitle.setText(Html.fromHtml(imageInfo.title));
			 
			 // Remotely download the image data in the background with Picassa lib
//			 Picasso.with(getContext()).load(imageInfo.thumbUrl).into(holder.ivImage);
			 double positionHeight = getPositionRatio(position);
			 
			 int backgroundIndex = position >= mBackgroundColors.size() ?
		                position % mBackgroundColors.size() : position;

		        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));

		        Log.d("adapter", "getView position:" + position + " h:" + positionHeight);
			 
			 
			 holder.ivImage.setHeightRatio(positionHeight);
			 Picasso.with(getContext()).load(getItem(position).thumbUrl).into(holder.ivImage);
//			 imageLoader.displayImage(imageInfo.url, holder.ivImage);
		 }			
		 catch(Exception e){
			 e.printStackTrace();
		 }
		 // return the convertView
		 return convertView;
	}
	
	private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d("ImageAdapter", "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }
 
    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
                                                    // the width
    }
	

}
