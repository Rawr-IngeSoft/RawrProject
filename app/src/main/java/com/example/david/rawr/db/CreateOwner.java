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


public class CreateOwner extends AsyncTask<String, Integer, String> {

    public static String username;
    public static String password;
    public static String name;
    public static String last;


    private static String url_create_owner = "http://178.62.233.249/rawr/create_owner.php";

    public CreateOwner(String username, String password, String name, String last) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.last = last;
    }

    /**
     * Creating Owner
     * */
    protected String doInBackground(String... args) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", this.username));
        params.add(new BasicNameValuePair("password", this.password));
        params.add(new BasicNameValuePair("name", this.name));
        params.add(new BasicNameValuePair("lastname", this.last));

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url_create_owner);
        String responseValue = "";
        try {
            post.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(post);
            Log.i("response ",response.getStatusLine().getStatusCode()+"");

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

}
