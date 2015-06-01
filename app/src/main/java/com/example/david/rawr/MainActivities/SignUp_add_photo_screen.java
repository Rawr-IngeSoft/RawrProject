package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.david.rawr.Interfaces.UploadPhotoResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.Tasks.UploadPhoto;

import java.io.FileNotFoundException;
import java.util.ArrayList;

//REQ-048
public class SignUp_add_photo_screen extends Activity implements View.OnClickListener, UploadPhotoResponse {

    Button selectButton, nextButton, omiteButton;
    ImageView photoImageView;
    Bitmap bitmap = null;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pet_add_photo_screen);
        selectButton = (Button)findViewById(R.id.selectImage);
        nextButton = (Button)findViewById(R.id.next);
        omiteButton = (Button)findViewById(R.id.omit);
        photoImageView = (ImageView)findViewById(R.id.signUp_imageView);
        selectButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        omiteButton.setOnClickListener(this);
        sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
    }

    //REQ-048
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case(R.id.selectImage):
                intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            break;
            case(R.id.next):
                if(bitmap != null){
                    UploadPhoto uploadPhoto = new UploadPhoto(bitmap, sharedpreferences.getString("petUsername",""), this );
                    uploadPhoto.execute();
                }
            break;
            case (R.id.omit):
                intent = new Intent(this, Newsfeed_screen.class);
                this.finish();
                startActivity(intent);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("pictureUri", targetUri.toString());
            editor.commit();
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                photoImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Error loading the photo", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void uploadFinish(ArrayList<String> response) {
        String pictureUri = "http://178.62.233.249/photos/" + sharedpreferences.getString("petUsername","") + response.get(1);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("pictureUri", pictureUri);
        editor.commit();
        Intent intent = new Intent(SignUp_add_photo_screen.this, Newsfeed_screen.class);
        startActivity(intent);
        this.finish();
    }
}
