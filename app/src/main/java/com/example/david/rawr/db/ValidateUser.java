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
 * Created by david on 05/04/2015.
 */
public class ValidateUser extends AsyncTask<String, Integer, String> {

    private String user;
    private String pass;
    private HttpResponse response;
    private static String url_validate_user = "http://178.62.233.249/rawr/validate_user.php";

    public ValidateUser(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    /**
     * Validating user
     */

    protected String doInBackground(String... args) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", this.user));
        params.add(new BasicNameValuePair("password", this.pass));

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url_validate_user);
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
