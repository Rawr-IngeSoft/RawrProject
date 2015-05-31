package com.example.david.rawr.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.david.rawr.Interfaces.GetFriendRequestsResponse;
import com.example.david.rawr.Interfaces.GetPostsResponse;
import com.example.david.rawr.Models.FriendRequest;
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
 * Created by david on 21/05/2015.
 */
public class GetFriendRequests extends AsyncTask<String, Integer, String> {
    String idPet;
    static ArrayList<FriendRequest> friendRequests;
    GetFriendRequestsResponse getFriendRequestsResponse;
    private static String url_get_friendRequests = "http://178.62.233.249/rawr/get_requests.php";
    public GetFriendRequests(String idPet, GetFriendRequestsResponse getFriendRequestsResponse) {
        this.idPet = idPet;
        this.getFriendRequestsResponse = getFriendRequestsResponse;
        friendRequests = new ArrayList<>();
    }

    @Override
    protected String doInBackground(String... params) {

        HttpClient client = new DefaultHttpClient();
        String query = url_get_friendRequests+"?username="+idPet;
        HttpGet get = new HttpGet(query);


        try {
            HttpResponse response = client.execute(get);
            JsonParser jsonParser = new JsonParser(response.getEntity().getContent());
            JSONObject jsonResponse= jsonParser.getjObject();
            if(jsonResponse != null){
                JSONArray jsonArray = jsonResponse.getJSONArray("requests");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    if(jo.getString("status").compareTo("accepted") != 0)
                        friendRequests.add(new FriendRequest(jo.getString("username_sender"), jo.getString("type"), jo.getString("race"), jo.getString("gender"), jo.getString("birth_date"), jo.getString("ownerName"), jo.getString("lastname"), jo.getString("name"), jo.getString("owner_username"), jo.getString("petPicture"), jo.getString("ownerPicture")));
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
        getFriendRequestsResponse.getRequestsFinish(friendRequests);
    }
}
