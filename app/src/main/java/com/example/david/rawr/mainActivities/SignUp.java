package com.example.david.rawr.mainActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.david.rawr.R;
import com.example.david.rawr.db.ValidateUser;

import java.util.concurrent.ExecutionException;

/**
 * Created by alfredo on 04/04/15.
 */
public class SignUp extends Activity implements View.OnClickListener {

    Button singUp;
    EditText userText, passText, nameText, lastnameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_wndow);
        singUp = (Button) findViewById(R.id.singUp_bt);
        userText = (EditText) findViewById(R.id.user_tx);
        passText = (EditText) findViewById(R.id.pass_tx);
        nameText = (EditText) findViewById(R.id.name_tx);
        lastnameText = (EditText) findViewById(R.id.lastname_tx);
        singUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = userText.getText().toString();
        String password = passText.getText().toString();
        String name = nameText.getText().toString();
        String lastname = lastnameText.getText().toString();
        if (username.compareTo("") == 0 || password.compareTo("") == 0){// || name.compareTo("") == 0 || lastname.compareTo("") == 0){
            Toast.makeText(this, "All the fields are required", Toast.LENGTH_SHORT);
        }else {
            ValidateUser validate = new ValidateUser(username, password);
            String status = null;
            try {
                status = validate.execute().get();
                if (status.compareTo("1") == 0) {
                    //TODO
                    //Create User
                } else {
                    Toast.makeText(this, "Sorry this username is in use", Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUp.this, LogIn.class );
        startActivity(intent);
        this.finish();
    }
}
