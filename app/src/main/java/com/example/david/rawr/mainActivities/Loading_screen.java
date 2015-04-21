package com.example.david.rawr.mainActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.rawr.R;
import com.example.david.rawr.db.ValidateUser;

import java.util.Timer;
import java.util.TimerTask;


public class Loading_screen extends Activity {

    TextView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        ImageView img = (ImageView)findViewById(R.id.animationImageView);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();
        Timer timer = new Timer();

        Intent intent = getIntent();
        if(intent.getStringExtra("username")!=null) {
            String username = intent.getStringExtra("username");
            String password = intent.getStringExtra("password");
            ValidateUser validate = new ValidateUser(username, password, this);
            validate.execute();

        }else{

            //espera para que aparezxa
            Intent intent1 = new Intent(Loading_screen.this, CreatePet_window.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);

        }


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
