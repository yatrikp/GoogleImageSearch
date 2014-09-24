package com.codepath.googleimagesearch.adapters;

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
import com.squareup.picasso.Picasso;

public class ImageResultAdapter extends ArrayAdapter<ImageResult> {
	
	private final LayoutInflater mLayoutInflater;
	private final Random mRandom;
	private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
	
	public static class ViewHolder {
		DynamicHeightImageView ivImage;
		
    }

	public ImageResultAdapter(Context context, List<ImageResult> objects) {
		super(context, android.R.layout.simple_list_item_1, objects);
		this.mLayoutInflater = LayoutInflater.from(context);
		this.mRandom = new Random();		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
		 final ImageResult imageInfo = getItem(position);
		 if (convertView == null){
			 convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
			 holder = new ViewHolder();
			 holder.ivImage = (DynamicHeightImageView)convertView.findViewById(R.id.ivImage);
			 convertView.setTag(holder);
		 }
		 else{
             holder = (ViewHolder) convertView.getTag();
	     }
		 
		 try{
			 double positionHeight = getPositionRatio(position, imageInfo.thumbWidth, imageInfo.thumbHeight);
			 convertView.setBackgroundResource(android.R.color.background_light);
			 holder.ivImage.setHeightRatio(positionHeight);
			// Remotely download the image data in the background with Picassa lib
			 Picasso.with(getContext()).load(imageInfo.thumbUrl).into(holder.ivImage);
		 }			
		 catch(Exception e){
			 e.printStackTrace();
		 }
		 // return the convertView
		 return convertView;
	}
	
	private double getPositionRatio(final int position, final int thumbWidth, final int thumbHeight) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
//            ratio = (thumbHeight/1000) + 1.0;
            
            sPositionHeightRatios.append(position, ratio);
            Log.d("ImageAdapter", "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }
 
    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 0.5; // height will be 1.0 - 1.5
                                                    // the width
    }	

}
