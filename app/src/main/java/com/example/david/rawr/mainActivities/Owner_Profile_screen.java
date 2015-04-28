package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.rawr.Adapters.PetChooseViewPagerAdapter;
import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.Interfaces.UploadPhotoResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.db.GetPhoto;
import com.example.david.rawr.models.Pet;
import com.example.david.rawr.otherClasses.RoundImage;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


// REQ-029
public class Owner_Profile_screen extends FragmentActivity implements View.OnClickListener,UploadPhotoResponse, GetPhotoResponse{

    ImageView photo;
    TextView birthdayText, nameText, addressText, lastNameText;
    Button logOut;
    SharedPreferences sharedpreferences;
    ViewPager petProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__profile_screen);
        logOut = (Button)findViewById(R.id.owner_profile_log_out);
        logOut.setOnClickListener(this);
        photo = (ImageView)findViewById(R.id.picture);
        birthdayText = (TextView)findViewById(R.id.owner_profile_birthday);
        nameText = (TextView)findViewById(R.id.owner_profile_name);
        lastNameText = (TextView)findViewById(R.id.owner_profile_lastName);
        addressText = (TextView)findViewById(R.id.owner_profile_address);
        petProfile = (ViewPager)findViewById(R.id.owner_profile_viewPager);
        FragmentManager fm = getSupportFragmentManager();
        ArrayList<Pet> petList = new ArrayList<Pet>();
        sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        petList.add(new Pet("asdas","Fabian","Dog","--/--/--",sharedpreferences.getString("pictureUri", "")));
        petList.add(new Pet("asdas","Stiven","Dog","--/--/--",sharedpreferences.getString("pictureUri", "")));
        petList.add(new Pet("asdas","Felipe","Dog","--/--/--",sharedpreferences.getString("pictureUri", "")));
        PetChooseViewPagerAdapter petChooseViewPagerAdapter = new PetChooseViewPagerAdapter(fm, petList );
        petProfile.setAdapter(petChooseViewPagerAdapter);
        // TODO get birthday dat
        //if (sharedpreferences.contains("username")) {
            birthdayText.setText("08/07/1993");
        //}
        if(sharedpreferences.contains("name")) {
            nameText.setText(sharedpreferences.getString("name", "") + " " );
        }
        if(sharedpreferences.contains("lastName")){
            lastNameText.setText(sharedpreferences.getString("lastName", ""));
        }
        if(sharedpreferences.contains("pictureUri")){
            GetPhoto getPhoto = new GetPhoto(sharedpreferences.getString("pictureUri", ""), this);
            try {
                Bitmap bitmap = getPhoto.execute().get();
                if(bitmap != null)
                    photo.setImageBitmap(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public void uploadFinish(ArrayList<String> response) {
        String status = response.get(0);
        if (status.compareTo("1") == 0) {
            Toast.makeText(this, "upload picture successful", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "upload picture error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getPhotoFinish(Bitmap bitmap) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.owner_profile_log_out:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove("username");
                editor.remove("name");
                editor.remove("lastName");
                editor.remove("pictureUri");
                editor.commit();
                if(LoginManager.getInstance() != null) {
                    LoginManager.getInstance().logOut();
                }
                Intent intent = new Intent(this, LogIn_screen.class );
                startActivity(intent);
                this.finish();
                break;
        }

    }
}
