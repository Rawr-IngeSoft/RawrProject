package com.example.david.rawr.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by david on 26/04/2015.
 */
public class GetPhoto extends AsyncTask<String, Integer, Bitmap> {

    String pictureUri = "http://178.62.233.249/photos/";
    GetPhotoResponse getPhotoResponse;

    public GetPhoto(String pictureUri, String username, GetPhotoResponse getPhotoResponse) {
        this.pictureUri += username + "/" + pictureUri;
        this.getPhotoResponse = getPhotoResponse;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        InputStream in;
        Bitmap bitmap = null;
        if (!pictureUri.equals("null")) {
            try {
                in = new URL(pictureUri).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap responseValue) {
        getPhotoResponse.getPhotoFinish(responseValue);
    }
}
