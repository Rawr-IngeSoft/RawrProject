package com.example.david.rawr.mainActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.rawr.R;
import com.example.david.rawr.db.DbMethods;
import com.example.david.rawr.db.ValidateUser;

import java.util.concurrent.ExecutionException;


public class LogIn extends Activity implements View.OnClickListener {

    Button logIn;
    EditText userText, passText;
    TextView signUp, forgotButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_window);
        logIn = (Button) findViewById(R.id.logInButton);
        userText = (EditText) findViewById(R.id.userText);
        passText = (EditText) findViewById(R.id.passText);
        signUp = (TextView) findViewById(R.id.signUp);
        forgotButton = (TextView) findViewById(R.id.forgotPassButton);
        logIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = userText.getText().toString();
        String password = passText.getText().toString();
        Intent intent;
        switch(v.getId()){
            case (R.id.signUp):
                Toast.makeText(this, "Sing Up", Toast.LENGTH_SHORT).show();
                intent = new Intent(LogIn.this, SingUp.class );
                startActivity(intent);
                this.finish();
                break;
            case (R.id.logInButton):
                ValidateUser validate = new ValidateUser(username, password);
                try {
                    String status = validate.execute().get();
                    if(status.compareTo("1") == 0) {
                        intent = new Intent(LogIn.this, createPet_window.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                        this.finish();
                    }else{
                        Toast.makeText(this, "Sorry wrong username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                break;
            case (R.id.forgotPassButton):
                Toast.makeText(this, "Sorry i don't know it", Toast.LENGTH_LONG).show();
                break;
        }
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
