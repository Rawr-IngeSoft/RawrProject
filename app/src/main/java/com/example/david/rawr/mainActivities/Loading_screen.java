package com.example.david.rawr.mainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.david.rawr.Interfaces.ValidateResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.db.ValidateUser;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Loading_screen extends Activity implements ValidateResponse{

    String username, password;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        Log.e("usernamePassed:", username);
        setContentView(R.layout.activity_loading_screen);
        ImageView img = (ImageView)findViewById(R.id.loading_screen_imageView_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String welcomeMsg = "Welcome " + sharedpreferences.getString("name","") + " " + sharedpreferences.getString("lastName","");
        Toast.makeText(getApplicationContext(), welcomeMsg, Toast.LENGTH_LONG).show();
        frameAnimation.start();

        // Validate User
        ValidateUser validateUser = new ValidateUser(username, password, this);
        validateUser.execute();
    }

    private void finishscreen() {
                this.finish();
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
    public void processFinish(ArrayList<String> data) {
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
            intent = new Intent(this, Loading_screen.class);
            startActivity(intent);
        }else{
            intent = new Intent(this, LogIn.class);
            Toast.makeText(this, "Wrong Username or Password", Toast.LENGTH_SHORT);
            startActivity(intent);
        }
        this.finish();
    }
}
