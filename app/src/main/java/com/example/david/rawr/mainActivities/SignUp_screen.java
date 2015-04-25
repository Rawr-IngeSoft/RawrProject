package com.example.david.rawr.mainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.david.rawr.R;

/**
 * Created by alfredo on 04/04/15.
 */
//REQ-001
public class SignUp_screen extends Activity implements View.OnClickListener {

    Button singUp;
    EditText userText, passText, nameText, lastnameText;
    AnimationDrawable loadingScreenAnimation;
    ImageView loadingScreen;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        singUp = (Button) findViewById(R.id.singUp_bt);
        userText = (EditText) findViewById(R.id.user_tx);
        passText = (EditText) findViewById(R.id.pass_tx);
        nameText = (EditText) findViewById(R.id.name_tx);
        lastnameText = (EditText) findViewById(R.id.lastname_tx);
        sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        loadingScreen = (ImageView) findViewById(R.id.loadingAnimation);
        loadingScreen.setBackgroundResource(R.drawable.frame_animation);
        loadingScreenAnimation = (AnimationDrawable) loadingScreen.getBackground();
        loadingScreenAnimation.start();
        singUp.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent;
        intent = new Intent(this, LogIn_screen.class);
        startActivity(intent);
        this.finish();
    }
    // REQ-001
    @Override
    public void onClick(View v) {
        String username = userText.getText().toString();
        String password = passText.getText().toString();
        String name = nameText.getText().toString();
        String lastName = lastnameText.getText().toString();
        loadingScreenAnimation.start();
        Intent intent = new Intent(this, Loading_screen.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("name", name);
        intent.putExtra("lastName", lastName);
        intent.putExtra("serviceType", "signUp");
        startActivity(intent);

    }

}
