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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 15/04/2015.
 */
public class CreatePet extends AsyncTask<String, Integer, String> {

    private String petName;
    private String petType;
    private String owner;
    private HttpResponse response;
    private static String url_create_pet = "http://178.62.233.249/rawr/create_pet.php";

    public CreatePet(String petName, String petType, String owner) {
        this.petName = petName;
        this.petType = petType;
        this.owner = owner;
    }

    protected String doInBackground(String... args) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", this.petName));
        params.add(new BasicNameValuePair("type", this.petType));
        params.add(new BasicNameValuePair("owner", this.owner));

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
        return responseValue;
    }

    /**
     * After completing background task
     * *
     */
    protected void onPostExecute(String responseValue) {

    }

}
