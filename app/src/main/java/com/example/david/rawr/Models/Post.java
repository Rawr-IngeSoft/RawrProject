package com.example.david.rawr.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.Tasks.GetPhoto;

/**
 * Created by david on 24/04/2015.
 */
public class Post implements GetPhotoResponse{
    private String petUsername, petName, text, date;
    private Bitmap photoBitmap = null, senderPhotoBitmap = null;
    private boolean withoutPhoto = false;

    public Post(String petUsername, String petName, String text, String date, String photo, String senderPhoto) {
        this.petUsername = petUsername;
        this.petName = petName;
        this.text = text;
        if (date.compareTo("null") != 0) {
            this.date = date;
        }else{
            this.date = "unknown";
        }
        if(photo.compareTo("null") != 0) {
            new GetPhoto(photo, petUsername, this).execute();
        }else {
            withoutPhoto = true;
        }
        if(senderPhoto.compareTo("null") != 0) {
            new GetPhoto(senderPhoto, petUsername, this).execute();
        }
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    public Bitmap getSenderPhotoBitmap() {
        return senderPhotoBitmap;
    }

    public String getPetUsername() {
        return petUsername;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    @Override
    public void getPhotoFinish(Bitmap bitmap) {
        if (this.photoBitmap != null || withoutPhoto == true){
            this.senderPhotoBitmap = bitmap;
        }else {
            this.photoBitmap = bitmap;
        }
    }
}
