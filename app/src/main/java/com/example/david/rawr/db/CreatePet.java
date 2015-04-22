package com.example.david.rawr.db;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 15/04/2015.
 */
public class CreatePet extends AsyncTask<String, Integer, String> {

    private final String username;
    private final String petName;
    private final String petType;
    private final String owner;
    private HttpResponse response;
    private static String url_create_pet = "http://178.62.233.249/rawr/create_pet.php";
    private JSONObject jsonResponse;

    public CreatePet(String username, String petName, String petType, String owner) {
        this.username = username;
        this.petName = petName;
        this.petType = petType;
        this.owner = owner;
    }

    protected String doInBackground(String... args) {
        String status = null;
        JSONObject jsonObjSend = new JSONObject();
        try {
            jsonObjSend.put("username", this.username);
            jsonObjSend.put("type", this.petType);
            jsonObjSend.put("name", this.petName);
            jsonObjSend.put("owner_username", this.owner);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url_create_pet);

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
        return status;
        /*
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", this.username));
        params.add(new BasicNameValuePair("name", this.petName));
        params.add(new BasicNameValuePair("type", this.petType));
        params.add(new BasicNameValuePair("owner_username", this.owner));

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url_create_pet);
        String responseValue = "";
        try {
            post.setEntity(new UrlEncodedFormEntity(params));
            response = client.execute(post);
            Header header = response.getFirstHeader("Content-Length");
            Log.i(header.getName(), header.getValue());
            responseValue = header.getValue();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseValue;*/
    }

    /**
     * After completing background task
     * *
     */
    protected void onPostExecute(String responseValue) {

    }

    public JSONObject getJsonResponse() {
        return jsonResponse;
    }
}
