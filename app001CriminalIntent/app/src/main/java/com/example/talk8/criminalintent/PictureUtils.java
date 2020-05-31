package com.example.talk8.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

//主要用来缩放图片，以避免OOM
public class PictureUtils {
    public static Bitmap getScaledBitmap(String path,int destWidth,int destHight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if(srcHeight >destHight || srcWidth >destWidth) {
            float heightScale = srcHeight/destHight;
            float widthScale = srcWidth/destWidth;
            inSampleSize = Math.round(heightScale>widthScale ?heightScale:widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path,options);
    }

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);

        return getScaledBitmap(path,point.x,point.y);
    }
}
