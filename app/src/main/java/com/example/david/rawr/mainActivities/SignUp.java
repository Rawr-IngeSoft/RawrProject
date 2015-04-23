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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.david.rawr.R;
import com.example.david.rawr.db.CreateOwner;

import java.util.concurrent.ExecutionException;

/**
 * Created by alfredo on 04/04/15.
 */
//REQ-001
public class SignUp extends Activity implements View.OnClickListener {

    Button singUp;
    EditText userText, passText, nameText, lastnameText;
    AnimationDrawable loadingScreenAnimation;
    ImageView loadingScreen;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_wndow);
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

    private void setSharedPreferences(String username, String name, String lastName){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("username", username);
        editor.putString("name", name);
        editor.putString("lastName", lastName);
        editor.commit();
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent;
        intent = new Intent(this, LogIn.class);
        startActivity(intent);
        finish_screen();
    }
    // REQ-001
    @Override
    public void onClick(View v) {
        String username = userText.getText().toString();
        String password = passText.getText().toString();
        String name = nameText.getText().toString();
        String lastname = lastnameText.getText().toString();
        loadingScreenAnimation.start();
        Toast.makeText(this, "Sign Up", Toast.LENGTH_LONG).show();
        CreateOwner createOwner = new CreateOwner(username, password, name, lastname);

        try {
            //TODO read response with json
            String status = createOwner.execute().get();
            Log.i("status--->", status);
            if (status.equals("1")){
                Toast.makeText(getApplicationContext(), "Owner Created", Toast.LENGTH_SHORT).show();
                Intent  intent = new Intent(SignUp.this, sign_up_add_photo_window.class );
                setSharedPreferences(username, name, lastname);
                startActivity(intent);
                finish_screen();

            }else{
                Toast.makeText(getApplicationContext(), "Error Creating Owner", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void finish_screen(){
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
