package com.example.simon.usecamera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;


public class FilterUtils {

    static final int COLOR_FACTOR = 50;
    static final int ALPHA = 255;
    static final String TAG = "Debug Log:";


    public static Bitmap imageToBitmap(ImageView imageView) {

        BitmapDrawable abm = (BitmapDrawable) imageView.getDrawable();
        Bitmap bmp = abm.getBitmap();

        // Create a bitmap of the same size
        Bitmap newBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        // Create a canvas for new bitmap
        Canvas canvas = new Canvas(newBmp);
        // Draw old bitmap on canvas
        canvas.drawBitmap(bmp, 0, 0, new Paint());
        return newBmp;
    }


    public static Bitmap modifyPixels(Bitmap newBmp) {
//        Log.d(TAG, "***** NEW MODIFICATION *****" );
        for (int i = 0; i < newBmp.getWidth(); i++) {
            for (int j = 0; j < newBmp.getHeight(); j++) {

                int color = newBmp.getPixel(i, j);

//                Log.d(TAG, "** NEW PIXEL **" );
//                Log.d(TAG, "color: " + color);

                int red = (color << 8 >>> 24);
                int green = (color << 16 >>> 24);
                int blue = (color << 24 >>> 24);

//                Log.d(TAG, "red: " + red);
//                Log.d(TAG, "green: " + green);
//                Log.d(TAG, "blue: " + blue);

                if (red <= green) {
//                    red = red + COLOR_FACTOR;
//                    green = green + COLOR_FACTOR;
//                    blue = blue + COLOR_FACTOR;

                    red = red * 9 / 8;
                    green = green * 9 / 8;
                    blue = blue * 9 / 8;

                } else {
//                    red = red - COLOR_FACTOR;
//                    green = green - COLOR_FACTOR;
//                    blue = blue - COLOR_FACTOR;

                    red = red * 8 / 9;
                    green = green * 8 / 9;
                    blue = blue * 8 / 9;
                }

                if (red > 255) red = 255;
                if (green > 255) green = 255;
                if (blue > 255) blue = 255;

//                Log.d(TAG, "FINAL COLOR: " + color);
//                Log.d(TAG, "red: " + red);
//                Log.d(TAG, "green: " + green);
//                Log.d(TAG, "blue: " + blue);

                newBmp.setPixel(i, j, Color.argb(ALPHA, red, green, blue));
            }
        }
        return newBmp;
    }

}
