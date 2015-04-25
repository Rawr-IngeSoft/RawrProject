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
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.rawr.R;
import com.example.david.rawr.otherClasses.RoundImage;
import com.facebook.login.LoginManager;

import java.io.FileNotFoundException;

// REQ-029
public class Owner_Profile_screen extends Activity implements View.OnClickListener{

    ImageView photo;
    TextView usernameText, nameText, lastNameText;
    Button logOutButton, createPetButton;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__profile_screen);
        photo = (ImageView)findViewById(R.id.picture);
        usernameText = (TextView)findViewById(R.id.username);
        nameText = (TextView)findViewById(R.id.name);
        lastNameText = (TextView)findViewById(R.id.lastName);
        logOutButton = (Button)findViewById(R.id.logOutButton);
        createPetButton = (Button)findViewById(R.id.activity_owner_profile_createPetButton);
        logOutButton.setOnClickListener(this);
        createPetButton.setOnClickListener(this);
        sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("username")) {
            usernameText.setText(sharedpreferences.getString("username", ""));
        }
        if(sharedpreferences.contains("name")) {
            nameText.setText(sharedpreferences.getString("name", ""));
        }
        if(sharedpreferences.contains("lastName")) {
            lastNameText.setText(sharedpreferences.getString("lastName", ""));
        }
        if(sharedpreferences.contains("pictureUri")){
            Uri pictureUri = Uri.parse(sharedpreferences.getString("pictureUri",""));
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(pictureUri));
                photo.setImageBitmap(RoundImage.getRoundedShape(bitmap));
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Error loading the picture", Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case(R.id.logOutButton):
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove("username");
                editor.remove("name");
                editor.remove("lastName");
                editor.remove("pictureUri");
                editor.commit();
                if(LoginManager.getInstance() != null) {
                    LoginManager.getInstance().logOut();
                }
                intent = new Intent(this, LogIn_screen.class );
                startActivity(intent);
                this.finish();
            break;
            case (R.id.activity_owner_profile_createPetButton):
                intent = new Intent(this, CreatePet_screen.class );
                startActivity(intent);
                this.finish();
            break;
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
