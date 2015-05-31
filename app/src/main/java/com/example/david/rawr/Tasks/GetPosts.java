package com.example.david.rawr.Tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.Interfaces.GetPostsResponse;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Estudiante on 24/04/2015.
 */
public class GetPosts extends AsyncTask<String, Integer, String> implements GetPhotoResponse{

    String idPet;
    ArrayList<Post> postArrayList;
    GetPostsResponse getPostsResponse;
    private static String url_get_posts = "http://178.62.233.249/rawr/get_posts.php";
    public GetPosts(String idPet, GetPostsResponse getPostsResponse) {
        this.idPet = idPet;
        this.getPostsResponse = getPostsResponse;
        postArrayList = new ArrayList<>();
    }

    @Override
    protected String doInBackground(String... params) {

        HttpClient client = new DefaultHttpClient();
        String query = url_get_posts+"?username="+idPet;
        HttpGet get = new HttpGet(query);


        try {
            HttpResponse response = client.execute(get);
            JsonParser jsonParser = new JsonParser(response.getEntity().getContent());
            JSONObject jsonResponse= jsonParser.getjObject();
            if(jsonResponse != null){
                JSONArray jsonArray = jsonResponse.getJSONArray("posts");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Post postToAdd = new Post(jo.getString("idPet"), jo.getString("name"), jo.getString("text"), jo.getString("date"), jo.getString("photo"), jo.getString("photoProfile"));
                    postArrayList.add(0,postToAdd);
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
        getPostsResponse.getPostsFinish(postArrayList);
    }

    @Override
    public void getPhotoFinish(Bitmap bitmap) {

    }
}
