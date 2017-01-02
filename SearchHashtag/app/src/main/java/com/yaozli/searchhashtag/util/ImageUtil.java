package com.yaozli.searchhashtag.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtil {

    public static Bitmap getBitmapFromUrl(String url){

        URL imageUrl = null;
        HttpURLConnection conn = null;
        try {

            imageUrl = new URL(url);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            Bitmap imagen = BitmapFactory.decodeStream(conn.getInputStream());
            return imagen;

        } catch (IOException e) {

            return null;

        }
    }
}
