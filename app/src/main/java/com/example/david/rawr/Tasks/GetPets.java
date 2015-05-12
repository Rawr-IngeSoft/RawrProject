package com.example.david.rawr.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.david.rawr.Interfaces.GetPetsResponse;
import com.example.david.rawr.Interfaces.GetPostsResponse;
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

public class GetPets extends AsyncTask<String, Integer, String> {

    private static String url_get_pets = "http://178.62.233.249/rawr/get_pets.php";
    private String username;
    private GetPetsResponse getPetsResponse;
    private ArrayList<Pet> pets;

    public GetPets(String username, GetPetsResponse getPetsResponse) {
        this.username = username;
        this.getPetsResponse = getPetsResponse;
        pets = new ArrayList<>();
    }


    @Override
    protected String doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url_get_pets+"?username="+username);

        try {
            HttpResponse response = client.execute(get);
            JsonParser jsonParser = new JsonParser(response.getEntity().getContent());
            JSONObject jsonResponse= jsonParser.getjObject();
            if(jsonResponse != null){
                JSONArray jsonArray = jsonResponse.getJSONArray("pets");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    Pet petToAdd = new Pet(jo.getString("username"), jo.getString("name"), jo.getString("type"), jo.getString("birth_date"), jo.getString("path"), jo.getString("gender"));
                    pets.add(petToAdd);
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
        getPetsResponse.getPetsFinish(pets);
    }
}
