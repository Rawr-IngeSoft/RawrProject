package com.example.david.rawr.Tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.david.rawr.Interfaces.CreatePetResponse;
import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.Interfaces.SendFriendRequestResponse;
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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Created by david on 31/05/2015.
 */
public class SendFriendRequest extends AsyncTask<String, Integer, String> {

    String sendRequestUri = "http://178.62.233.249/rawr/send_request.php";
    String senderUsername, receierUsername;
    Context context;
    SendFriendRequestResponse sendFriendRequestResponse;

    public SendFriendRequest(String senderUsername, String receierUsername, Context context, SendFriendRequestResponse sendFriendRequestResponse) {
        this.senderUsername = senderUsername;
        this.receierUsername = receierUsername;
        this.context = context;
        this.sendFriendRequestResponse = sendFriendRequestResponse;
    }

    protected String doInBackground(String... args) {
        String status = "0";
        try {
            // Building Parameters
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username_sender", this.receierUsername);
            jsonObject.put("username_receiver", this.senderUsername);
            jsonObject.put("text", "not implemented");
            jsonObject.put("date", "today");
            jsonObject.put("status", "pending");
            Log.e("sendRequest", jsonObject.toString());
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(sendRequestUri);
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
        sendFriendRequestResponse.sendFriendRequestFinish(responseValue);
    }
}
