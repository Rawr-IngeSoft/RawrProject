package com.example.david.rawr.Tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.david.rawr.Interfaces.AnswerRequestResponse;
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

/**
 * Created by david on 31/05/2015.
 * @Requirements REQ-024
 */
public class AnswerRequest extends AsyncTask<String, Integer, String> {

    private String sender;
    private String receiver;
    private String decision;
    private static String url_answerRequest = "http://178.62.233.249/rawr/answer_request.php";
    private JSONObject jsonResponse;

    public AnswerRequest(String sender, String receiver, String decision) {
        this.sender = sender;
        this.receiver = receiver;
        this.decision = decision;
    }

    /**
     * Creating Owner
     * */
    protected String doInBackground(String... args) {

        String status = null;
        JSONObject jsonObjSend = new JSONObject();
        try {
            jsonObjSend.put("sender", this.sender);
            jsonObjSend.put("receiver", this.receiver);
            jsonObjSend.put("status", this.decision);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url_answerRequest);

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

    protected void onPostExecute(String status) {
        Log.e("answerRequest", status);
    }
}
