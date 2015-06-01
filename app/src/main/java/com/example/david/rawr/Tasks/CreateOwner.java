package com.example.david.rawr.Tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.david.rawr.Interfaces.CreateResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

// @Requirement REQ-001
public class CreateOwner extends AsyncTask<String, Integer, String> {

    private static String username;
    private static String password;
    private static String name;
    private static String last;
    private CreateResponse createResponse;
    private SharedPreferences sharedPreferences;
    private static String url_create_owner = "http://178.62.233.249/rawr/create_owner.php";
    private JSONObject jsonResponse;

    public CreateOwner(String username, String password, String name, String last, CreateResponse createResponse, Context context) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.last = last;
        this.createResponse = createResponse;
        sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
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
            if (status.compareTo("1") == 0){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.putString("name",name);
                editor.putString("lastName",last);
                editor.commit();
            }
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

    protected void onPostExecute(String status) {
        createResponse.createFinish(status);
    }
}
