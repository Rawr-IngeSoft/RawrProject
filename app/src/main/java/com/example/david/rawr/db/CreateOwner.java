package com.example.david.rawr.db;

import android.os.AsyncTask;
import android.util.Log;

import com.example.david.rawr.Interfaces.CreateResponse;

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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

// REQ-001
public class CreateOwner extends AsyncTask<String, Integer, String> {

    private static String username;
    private static String password;
    private static String name;
    private static String last;
    private CreateResponse createResponse;

    private static String url_create_owner = "http://178.62.233.249/rawr/create_owner.php";
    private JSONObject jsonResponse;

    public CreateOwner(String username, String password, String name, String last, CreateResponse createResponse) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.last = last;
        this.createResponse = createResponse;
    }

    /**
     * Creating Owner
     * */
    protected String doInBackground(String... args) {

        String status = null;
        JSONObject jsonObjSend = new JSONObject();
        try {
            jsonObjSend.put("username", this.username);
            jsonObjSend.put("password", this.password);
            jsonObjSend.put("name", this.name);
            jsonObjSend.put("lastname", this.last);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url_create_owner);

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
        String msg = "Welcome " + name + " " + last;
        return msg;
    }

    protected void onPostExecute(String msg) {
        createResponse.createFinish(msg);
    }
}
