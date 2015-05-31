package com.example.david.rawr.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.david.rawr.Interfaces.GetFriendsResponse;
import com.example.david.rawr.Models.Friend;
import com.example.david.rawr.Models.Pet;
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
 * Created by david on 11/05/2015.
 */

public class GetFriends extends AsyncTask<String, Integer, String> {

    private static String url_get_friends = "http://178.62.233.249/rawr/get_friends.php";
    private String username;
    private GetFriendsResponse getFriendsResponse;
    private ArrayList<Friend> friends;
    public GetFriends(String username, GetFriendsResponse getFriendsResponse) {
        this.username = username;
        this.getFriendsResponse = getFriendsResponse;
        friends = new ArrayList<>();
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url_get_friends+"?username="+username);

        try {
            HttpResponse response = client.execute(get);
            JsonParser jsonParser = new JsonParser(response.getEntity().getContent());
            JSONObject jsonResponse= jsonParser.getjObject();
            if(jsonResponse != null){
                JSONArray jsonArray = jsonResponse.getJSONArray("friends");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Friend friendToAdd = new Friend(jo.getString("username"), jo.getString("name"), jo.getString("path"));
                    friends.add(friendToAdd);
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
        getFriendsResponse.getFriendsFinish(friends);
    }
}
