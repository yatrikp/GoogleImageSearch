package com.codepath.googleimagesearch.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class Util {
	private static String PRIMARY_FONT = "Roboto-Regular.ttf";
    private static String SPLASH_SCREEN_FONT = "Pacifico.ttf";

    public static void applyPrimaryFont(Context ctx, TextView textView) {
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/"
                + PRIMARY_FONT);
        textView.setTypeface(typeface);
    }

    public static void  applyDisplayScreenActionBarFont(Context ctx, TextView textview){
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/"
                + SPLASH_SCREEN_FONT);
        textview.setTypeface(typeface);
        
    }
}
