package com.example.david.rawr.db;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.david.rawr.mainActivities.CreatePet_window;
import com.example.david.rawr.mainActivities.Loading_screen;
import com.example.david.rawr.mainActivities.LogIn;
import com.facebook.internal.Validate;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.widget.Toast;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by david on 05/04/2015.
 */
public class ValidateUser extends AsyncTask<String, Integer, String> {

    private String user;
    private String pass;
    private HttpResponse response;
    private static String url_validate_user = "http://178.62.233.249/rawr/validate_user.php";
    private JSONObject jsonResponse;
    private Activity login;
    private Activity loadingScreen;


    public ValidateUser(String user, String pass, Activity activity) {
        this.user = user;
        this.pass = pass;
        this.login=activity;
    }

    /**
     * Validating user
     */

    protected String doInBackground(String... args) {
/*
       intent = new Intent(login, Loading_screen.class);
        login.startActivity(intent);
*/
        String status = null;
        JSONObject jsonObjSend = new JSONObject();
        try {
            jsonObjSend.put("username", this.user);
            jsonObjSend.put("password", this.pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url_validate_user);

       // post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        //post.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");


        jsonResponse = null;
        StringEntity se = null;
        try {
            se = new StringEntity(jsonObjSend.toString());
            post.setEntity(se);

            HttpResponse response;
            response = client.execute(post);
            JsonParser jsonParser = new JsonParser(response.getEntity().getContent());

            jsonResponse= jsonParser.getjObject();
            status = jsonResponse.getString("status");
            Log.i("Response ", jsonResponse.getString("status"));

            Log.i("Json resoponse","<JSONObject>\n"+jsonResponse.toString()+"\n</JSONObject>");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v("STAT", status);
        return status;
    }

    /**
     * After completing background task
     * *
     */
    protected void onPostExecute(String responseValue) {
        if(responseValue.equals("1")) {
            Intent intent = new Intent(login, CreatePet_window.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("username", this.user);
            login.startActivity(intent);

        }else{
            Intent intent = new Intent(login, LogIn.class);
            login.startActivity(intent);

            /*
            Intent intent = new Intent(login, LogIn.class);
            login.startActivity(intent);

        */
        }
    }


    public JSONObject getJsonResponse() {
        return jsonResponse;
    }
}
