package com.example.david.rawr.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

import com.example.david.rawr.MainActivities.Owner_Profile_screen;
import com.example.david.rawr.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by david on 25/04/2015.
 */
public class GetFacebookPhoto extends AsyncTask<String, Integer, Bitmap> {

    String pictureUri;
    public GetFacebookPhoto(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        InputStream in;
        Bitmap bitmap = null;
        try {
            in = new URL(pictureUri).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
