package com.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Bitmap虚化处理
 */

public class Blur {
    /**
     * 通过调用系统高斯模糊api的方法模糊
     *
     * @param bitmap    source bitmap
     * @param outBitmap out bitmap
     * @param radius    0 < radius <= 25
     * @param context   context
     * @return out bitmap
     */
    public Bitmap blurBitmap(Bitmap bitmap, Bitmap outBitmap, float radius, Context context) {
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        //Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(radius);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
//        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }

    /**
     * 比例压缩图片
     *
     * @param sourceBitmap 源bitmap
     * @param scaleFactor  大于1，将bitmap缩小
     * @return 缩小scaleFactor倍后的bitmap
     */
    public Bitmap compressBitmap(Bitmap sourceBitmap, float scaleFactor) {
        Bitmap overlay = Bitmap.createBitmap((int) (sourceBitmap.getWidth() / scaleFactor),
                (int) (sourceBitmap.getHeight() / scaleFactor), Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(0, 0);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return overlay;
    }

    /**
     * 改变图片对比度,达到使图片明暗变化的效果
     *
     * @param srcBitmap source bitmap
     * @param contrast  图片亮度，0：全黑；小于1，比原图暗；1.0f原图；大于1比原图亮
     * @return bitmap
     */
    public Bitmap darkBitmap(Bitmap srcBitmap, float contrast) {

        float offset = (float) 0.0; //picture RGB offset

        int imgHeight, imgWidth;
        imgHeight = srcBitmap.getHeight();
        imgWidth = srcBitmap.getWidth();

        Bitmap bmp = Bitmap.createBitmap(imgWidth, imgHeight, Config.ARGB_8888);
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{contrast, 0, 0, 0, offset,
                0, contrast, 0, 0, offset,
                0, 0, contrast, 0, offset,
                0, 0, 0, 1, 0});

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(srcBitmap, 0, 0, paint);

        return bmp;
    }
}
