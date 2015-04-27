package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.david.rawr.Interfaces.CreateResponse;
import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.Interfaces.UploadPhotoResponse;
import com.example.david.rawr.Interfaces.ValidateResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.db.CreateOwner;
import com.example.david.rawr.db.GetPhoto;
import com.example.david.rawr.db.UploadPhoto;
import com.example.david.rawr.db.ValidateUser;

import java.util.ArrayList;


public class Loading_screen extends Activity implements ValidateResponse, CreateResponse,UploadPhotoResponse,GetPhotoResponse {

    String username, serviceType;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_loading_screen);
        ImageView img = (ImageView)findViewById(R.id.loading_screen_imageView_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();
        serviceType = getIntent().getStringExtra("serviceType");
        if(serviceType.compareTo("logIn") == 0) {
            if (sharedpreferences.contains("username")) {
                Intent intent = new Intent(this, Newsfeed_screen.class);
                startActivity(intent);
                this.finish();
            }
            username = getIntent().getStringExtra("username");
            String password = getIntent().getStringExtra("password");
            // Validate User
            ValidateUser validateUser = new ValidateUser(username, password, this);
            validateUser.execute();
        }else if (serviceType.compareTo("facebook") == 0){
            // Create facebook user
            username = sharedpreferences.getString("username","");
            CreateOwner createOwner = new CreateOwner(username,"facebook",sharedpreferences.getString("name",""), sharedpreferences.getString("lastName",""),this, this);
            createOwner.execute();
        }else if (serviceType.compareTo("signUp") == 0){
            // Create Owner
            username = getIntent().getStringExtra("username");
            String password = getIntent().getStringExtra("password");
            String name = getIntent().getStringExtra("name");
            String lastName = getIntent().getStringExtra("lastName");
            CreateOwner createOwner = new CreateOwner(username,password,name, lastName,this,this);
            createOwner.execute();
        }else if (serviceType.compareTo("logged") == 0) {
            String welcomeMsg = "Welcome " + sharedpreferences.getString("name", "") + " " + sharedpreferences.getString("lastName", "");
            Toast.makeText(getApplicationContext(), welcomeMsg, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Newsfeed_screen.class);
            startActivity(intent);
            this.finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loading_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void validateFinish(ArrayList<String> data) {
        String status = data.get(0);
        Intent intent;
        if (status.compareTo("1") == 0){
            String name = data.get(1);
            String lastName = data.get(2);
            //save owner info in shared preferences
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("username", username);
            editor.putString("name", name );
            editor.putString("lastName", lastName );
            editor.commit();
            String welcomeMsg = "Welcome " + sharedpreferences.getString("name", "") + " " + sharedpreferences.getString("lastName", "");
            Toast.makeText(getApplicationContext(), welcomeMsg, Toast.LENGTH_LONG).show();
            intent = new Intent(this, Newsfeed_screen.class);
        }else{
            intent = new Intent(this, LogIn_screen.class);
            Toast.makeText(this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);
        this.finish();
    }

    @Override
    public void createFinish(String status) {
        Intent intent;
        if (serviceType.compareTo("facebook") == 0) {
            if (status.compareTo("1") == 0) {
                GetPhoto getFacebookPhoto = new GetPhoto(sharedpreferences.getString("pictureUri", ""), this);
                getFacebookPhoto.execute();
            }else{
                intent = new Intent(this, Newsfeed_screen.class);
                startActivity(intent);
                this.finish();
            }
        }else{
            if (status.compareTo("1") == 0) {
                String msg = "Welcome " + sharedpreferences.getString("name","") + " " + sharedpreferences.getString("lastName","");
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                intent = new Intent(this, sign_up_add_photo_screen.class);
            }else{
                Toast.makeText(this, "This username is already in use", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, SignUp_screen.class);
            }
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void uploadFinish(ArrayList<String> response) {
        String status = response.get(0);
        if (status.compareTo("1") == 0) {
            Log.e("Upload picture:", "Successful");
        }else {
            Log.e("Upload picture:", "Error");
        }
    }

    @Override
    public void getPhotoFinish(Bitmap bitmap) {
        UploadPhoto uploadPhoto = new UploadPhoto(bitmap, username, this);
        uploadPhoto.execute();
        Intent intent = new Intent(this, Newsfeed_screen.class);
        startActivity(intent);
        this.finish();
    }
}
