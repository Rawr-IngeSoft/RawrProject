package com.example.david.rawr.mainActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.rawr.R;
import com.example.david.rawr.db.DbMethods;


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
                // TODO
                Toast.makeText(this, "Sing Up", Toast.LENGTH_LONG).show();
                intent = new Intent(LogIn.this, SingUp.class );
                startActivity(intent);
                finish_screen();
                break;
            case (R.id.logInButton):
                // TODO
                DbMethods.login(username, password);
                /*intent = new Intent(LogIn.this, downloading_window.class );
                startActivity(intent);*/
                finish_screen();
                Toast.makeText(this, "Logged in", Toast.LENGTH_LONG).show();
                break;
            case (R.id.forgotPassButton):
                Toast.makeText(this, "Sorry i don't know it", Toast.LENGTH_LONG).show();
                break;
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
