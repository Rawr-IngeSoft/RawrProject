package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.david.rawr.otherClasses.Politics_screen;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// REQ-008
public class LogIn_screen extends Activity implements View.OnClickListener {

    Button logIn;
    EditText userText, passText;
    TextView signUp, forgotButton, politics;
    CallbackManager callbackManager;
    LoginButton faceLoginButton;
    String ownerName, ownerLastName, ownerId, ownerPicture;
    SharedPreferences sharedpreferences;

    /**
     * Revisa las conexiones con las que cuenta el telefono y sus respectivos estados
     * @param  ctx contexto actual de la vista
     * @return true si hay alguna conexion activa o false de lo contrario
     */
    public static boolean checkConnection(Context ctx) {
        boolean isConnected = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networks = connec.getAllNetworkInfo();
        for (NetworkInfo n: networks) {
            if (n.getState() == NetworkInfo.State.CONNECTED) {
                isConnected = true;
            }
        }
        return isConnected;
    }

    /**
     * Metodo auxiliar que imprime el hashkey que pide facebook.
     * Util para desarrolladores
     */
    private void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                        Log.e("MY_KEY_HASH:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        } }
    //---------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
    getHashKey();
        // ---------------

        setContentView(R.layout.activity_login_screen);
        logIn = (Button) findViewById(R.id.logInButton);
        userText = (EditText) findViewById(R.id.userText);
        passText = (EditText) findViewById(R.id.passText);
        signUp = (TextView) findViewById(R.id.signUp);
        politics = (TextView) findViewById(R.id.politics);
        forgotButton = (TextView) findViewById(R.id.forgotPassButton);
        logIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        politics.setOnClickListener(this);
        forgotButton.setOnClickListener(this);
        setupUI(this.findViewById(R.id.loginView));
        sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        // Si ya hay un username guardado en la base de datos significa que ya se ha logeado previamente
        // Se redirige a loading_screen con tipo de conexion logged
        if (sharedpreferences.contains("username")) {
            Intent intent = new Intent(LogIn_screen.this, Loading_screen.class);
            intent.putExtra("serviceType", "logged");
            startActivity(intent);
            this.finish();
        }
        // Facebook login button implementation
        faceLoginButton = (LoginButton)findViewById(R.id.faceLoginButton);
        faceLoginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        // Revisar si el telefono cuenta con algun servicio de internet
        if (checkConnection(this) == true) {
            // Manejo de boton de facebook. Se encarga de obtener la informacion de usuario de facebook.
            // Se redirige a loading_screen con tipo de conexion facebook
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
                                        ownerPicture = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                        Intent intent = new Intent(LogIn_screen.this, Loading_screen.class);
                                        intent.putExtra("serviceType", "facebook");
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("username", ownerId);
                                        editor.putString("name", ownerName);
                                        editor.putString("lastName", ownerLastName);
                                        editor.putString("ownerPictureUri", ownerPicture);
                                        editor.commit();
                                        startActivity(intent);
                                        LogIn_screen.this.finish();
                                    } catch (JSONException je) {
                                        je.printStackTrace();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,first_name, last_name, picture.type(large)");
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
        }else{
            Toast.makeText(this, "Turn on the internet and restart the app", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * listener de eventos tipo click
     * @param  v vista de donde proviene el evento
     */
    @Override
    public void onClick(View v) {
        String username = userText.getText().toString();
        String password = passText.getText().toString();
        Intent intent;
        // Se revisa que el telefono cuente con conexion a internet
        if(checkConnection(this) == true) {
            switch (v.getId()) {
                // En caso de que el evento sea disparado por el boton de signUp
                // se redirige a la pantalla de signUp_screen
                case (R.id.signUp):
                    Toast.makeText(this, "Sign Up", Toast.LENGTH_LONG).show();
                    intent = new Intent(LogIn_screen.this, SignUp_screen.class);
                    startActivity(intent);
                    finish_screen();
                    break;
                // En caso de que el evento sea disparado por el boton de logIn propio
                // se redirige a la pantalla LogIn_screen
                case (R.id.logInButton):
                    intent = new Intent(LogIn_screen.this, Loading_screen.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("serviceType", "logIn");
                    startActivity(intent);
                    finish_screen();
                    break;
                // En caso de que el evento sea disparado por el boton de olvido contrasenha
                // no esta implementado
                // TODO funcionalidad para recuperar contrasenha
                case (R.id.forgotPassButton):
                    Toast.makeText(this, "Sorry i don't know it", Toast.LENGTH_LONG).show();
                    break;
                case (R.id.politics):
                    intent = new Intent(LogIn_screen.this, Politics_screen.class);
                    startActivity(intent);
                    break;
            }
        }else{
            Toast.makeText(this, "You must be connect to the internet", Toast.LENGTH_LONG).show();
        }
    }

    // Eliminar vista actual
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

    // Funcion para ocultar el teclado
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    // Funcion para ocultar el teclado
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

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
