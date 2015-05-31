package com.example.david.rawr.Tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.david.rawr.Interfaces.CreatePetResponse;
import com.example.david.rawr.Interfaces.CreatePostResponse;
import com.example.david.rawr.Models.Pet;
import com.example.david.rawr.SQLite.SQLiteHelper;

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

/**
 * Created by david on 30/05/2015.
 */
public class CreatePost extends AsyncTask<String, Integer, String> {

    private final String username;
    private final String text;
    private final String postType;
    private final String idPhoto;
    private static String url_create_post = "http://178.62.233.249/rawr/create_post.php";
    private CreatePostResponse createPostResponse;
    private Context context;
    public CreatePost(String username, String postType, String idPhoto, String text,CreatePostResponse createPostResponse, Context context) {
        this.username = username;
        this.text = text;
        this.postType = postType;
        this.idPhoto = idPhoto;
        this.createPostResponse = createPostResponse;
        this.context = context;
    }

    protected String doInBackground(String... args) {
        String status = "0";
        try {
            // Building Parameters
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", this.username);
            jsonObject.put("text", this.text);
            jsonObject.put("type", this.postType);
            if(idPhoto.compareTo("null") != 0)
                jsonObject.put("idPhoto", idPhoto);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url_create_post);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(jsonObject.toString());
            post.setEntity(se);

            HttpResponse response;
            response = client.execute(post);
            JsonParser jsonParser = new JsonParser(response.getEntity().getContent());
            JSONObject jsonResponse= jsonParser.getjObject();
            status = jsonResponse.getString("status");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * After completing background task
     * *
     */
    protected void onPostExecute(String responseValue) {
        createPostResponse.createPostFinish(responseValue);
    }

}
