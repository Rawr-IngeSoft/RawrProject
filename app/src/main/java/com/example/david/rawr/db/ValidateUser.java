package com.example.david.rawr.db;

import android.os.AsyncTask;
import android.util.Log;

import com.example.david.rawr.Interfaces.ValidateResponse;

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
import java.util.concurrent.ExecutionException;

/**
 * Created by david on 05/04/2015.
 */
// REQ-008
public class ValidateUser extends AsyncTask<String, Integer, String> {

    private String user;
    private String pass;
    private HttpResponse response;
    private static String url_validate_user = "http://178.62.233.249/rawr/validate_user.php";
    private JSONObject jsonResponse;
    public ValidateResponse validateResponse = null;
    public ValidateUser(String user, String pass, ValidateResponse validateResponse) {
        this.user = user;
        this.pass = pass;
        this.validateResponse = validateResponse;
    }

    /**
     * Validating user
     */

    protected String doInBackground(String... args) {
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

        post.setHeader("Accept", "application/json");
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * After completing background task
     * *
     */
    protected void onPostExecute(String status) {
        ArrayList<String> data = new ArrayList<>();
        data.add(status);
        try {
            data.add(jsonResponse.getJSONObject("user").getString("name"));
            data.add(jsonResponse.getJSONObject("user").getString("lastname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        validateResponse.processFinish(data);

    }


    public JSONObject getJsonResponse() {
        return jsonResponse;
    }
}
