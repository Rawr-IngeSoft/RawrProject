package com.example.david.rawr.mainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.david.rawr.R;

import java.util.Timer;
import java.util.TimerTask;


public class Loading_screen extends Activity {

    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        ImageView img = (ImageView)findViewById(R.id.loading_screen_imageView_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String welcomeMsg = "Welcome " + sharedpreferences.getString("name","") + " " + sharedpreferences.getString("lastName","");
        Toast.makeText(getApplicationContext(), welcomeMsg, Toast.LENGTH_LONG).show();
        frameAnimation.start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Loading_screen.this, Owner_Profile_window.class );
                startActivity(intent);
                finishscreen();
            }
        }, 4000);
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
}
