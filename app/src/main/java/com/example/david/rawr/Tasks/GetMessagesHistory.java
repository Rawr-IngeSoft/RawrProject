package com.example.david.rawr.Tasks;

import android.os.AsyncTask;

import com.example.david.rawr.Interfaces.GetMessagesHistoryResponse;
import com.example.david.rawr.Interfaces.GetPostsResponse;
import com.example.david.rawr.Models.Message;
import com.example.david.rawr.Models.Post;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by david on 16/05/2015.
 */
public class GetMessagesHistory extends AsyncTask<String, String, String> {

    String idPet;
    ArrayList<Message> messagestArrayList;
    GetMessagesHistoryResponse getMessagesHistoryResponse;
    private static String url_get_messages = "http://178.62.233.249/rawr/get_messages.php";

    public GetMessagesHistory(String idPet, GetMessagesHistoryResponse getMessagesHistoryResponse) {
        this.idPet = idPet;
        this.getMessagesHistoryResponse = getMessagesHistoryResponse;
        messagestArrayList = new ArrayList<>();
    }

    @Override
    protected String doInBackground(String... params) {

        HttpClient client = new DefaultHttpClient();
        String query = url_get_messages+"?username="+idPet;
        HttpGet get = new HttpGet(query);


        try {
            HttpResponse response = client.execute(get);
            JsonParser jsonParser = new JsonParser(response.getEntity().getContent());
            JSONObject jsonResponse= jsonParser.getjObject();
            if(jsonResponse != null){
                if(jsonResponse.getString("status").compareTo("1") == 0) {
                    JSONArray jsonArray = jsonResponse.getJSONArray("messages");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Message msgtToAdd = new Message(jo.getString("text"), jo.getString("sender"), jo.getString("receiver"), jo.getString("status"), jo.getString("date"));
                        messagestArrayList.add(msgtToAdd);
                    }
                }
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
        return "";
    }

    protected void onPostExecute(String responseValue) {
        getMessagesHistoryResponse.getMessageHistoryFinish(messagestArrayList);
    }
}
