package com.example.david.rawr.mainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.rawr.R;
import com.example.david.rawr.db.CreateOwner;
import com.example.david.rawr.db.ValidateUser;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class LogIn extends Activity implements View.OnClickListener {

    Button logIn;
    EditText userText, passText;
    TextView signUp, forgotButton;
    CallbackManager callbackManager;
    LoginButton faceLoginButton;
    String ownerName, ownerLastName, ownerId;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_window);
        logIn = (Button) findViewById(R.id.logInButton);
        userText = (EditText) findViewById(R.id.userText);
        passText = (EditText) findViewById(R.id.passText);
        signUp = (TextView) findViewById(R.id.signUp);
        forgotButton = (TextView) findViewById(R.id.forgotPassButton);
        logIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotButton.setOnClickListener(this);
        setupUI(this.findViewById(R.id.loginView));

        sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        // Facebook login button implementation
        faceLoginButton = (LoginButton)findViewById(R.id.faceLoginButton);
        faceLoginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        faceLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    ownerId = object.getString("id");
                                    ownerName = object.getString("first_name");
                                    ownerLastName = object.getString("last_name");
                                    CreateOwner createOwner = new CreateOwner(ownerId, "facebook", ownerName, ownerLastName);
                                    //TODO read response with json
                                    String responseValue = createOwner.execute().get();
                                    if (responseValue.compareTo("Error inserting new User in database") == 0){
                                        Toast.makeText(getApplicationContext(), "Error Creating Owner", Toast.LENGTH_SHORT).show();
                                    }
                                    Intent intent = new Intent(LogIn.this, Loading_screen.class);
                                    intent.putExtra("username", ownerId);
                                    String welcomeMsg = "Welcome " + ownerName + " " + ownerLastName;
                                    Toast.makeText(getApplicationContext(), welcomeMsg, Toast.LENGTH_LONG).show();
                                    startActivity(intent);
                                    LogIn.this.finish();
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                } catch (ExecutionException e) {
                                        e.printStackTrace();
                                }catch(JSONException je) {
                                    je.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name, last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });

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
                intent = new Intent(LogIn.this, SignUp.class );
                startActivity(intent);
                finish_screen();
                break;
            case (R.id.logInButton):
                ValidateUser validate = new ValidateUser(username, password);
                try {
                    String status = validate.execute().get();
                    Log.i("status--->", status);
                    if(status.equals("1")) {
                        JSONObject jsonResponse = validate.getJsonResponse();

                        //save owner info in shared preferences
                        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("username", username);
                        editor.putString("name", jsonResponse.getJSONObject("user").getString("name") );
                        editor.putString("lastname", jsonResponse.getJSONObject("user").getString("lastname") );
                        editor.commit();


                        intent = new Intent(LogIn.this, Loading_screen.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        Toast.makeText(this, "Logged in", Toast.LENGTH_LONG).show();
                        finish_screen();
                    }else{
                        Toast.makeText(this, "Sorry wrong username or password", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                  //ojo con esto
                    hideSoftKeyboard();
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
