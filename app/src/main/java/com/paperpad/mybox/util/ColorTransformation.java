package com.paperpad.mybox.util;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.squareup.picasso.Transformation;

/**
 * Copied by euphordev02 from paulruiz on 10/10/2014.
 */

public class ColorTransformation extends BitmapTransformation {

    private int color = 0;

    public ColorTransformation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if( color == 0 ) {
            return toTransform;
        }

        BitmapDrawable drawable = new BitmapDrawable(Resources.getSystem(), toTransform);
        Bitmap result = Bitmap.createBitmap( drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888 );
        Canvas canvas = new Canvas(result);
        drawable.setBounds( 0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() );
        drawable.setColorFilter( color, PorterDuff.Mode.SRC_IN );
        drawable.setAntiAlias(true);
        drawable.draw(canvas);
        drawable.setColorFilter(null);
        drawable.setCallback(null);

        if( result != toTransform ) {
            toTransform.recycle();
        }

        return result;
    }

    public ColorTransformation(Context context, int color) {
        super(context);
        setColor( color );
    }

    public void setColor( int color ) {
        this.color = color;
    }

    public void setColorFromRes( Context context, int colorResId ) {
        setColor( context.getResources().getColor( colorResId ) );
    }

    public int getColor() {
        return color;
    }

    @Override
    public String getId() {
        return "DrawableColor:" + color;
    }
}