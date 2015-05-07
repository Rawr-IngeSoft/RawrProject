package com.example.david.rawr.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.david.rawr.Interfaces.CreatePetResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by David on 15/04/2015.
 */
// REQ-014
public class CreatePet extends AsyncTask<String, Integer, String> {

    private final String username;
    private final String petName;
    private final String petType;
    private final String owner;
    private HttpResponse response;
    private static String url_create_pet = "http://178.62.233.249/rawr/create_pet.php";
    private CreatePetResponse createPetResponse;
    private Context context;
    public CreatePet(String username, String petName, String petType, String owner, CreatePetResponse createPetResponse, Context context) {
        this.username = username;
        this.petName = petName;
        this.petType = petType;
        this.owner = owner;
        this.createPetResponse = createPetResponse;
        this.context = context;
    }

    protected String doInBackground(String... args) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", this.username));
        params.add(new BasicNameValuePair("name", this.petName));
        params.add(new BasicNameValuePair("type", this.petType));
        params.add(new BasicNameValuePair("owner_username", this.owner));

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url_create_pet);
        String responseValue = "";
        try {
            post.setEntity(new UrlEncodedFormEntity(params));
            response = client.execute(post);
            Header header = response.getFirstHeader("Content-Length");
            responseValue = header.getValue();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseValue;
    }

    /**
     * After completing background task
     * *
     */
    protected void onPostExecute(String responseValue) {
        if (responseValue.compareTo("1") == 0){
        }else{
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =  sharedPreferences.edit();
            editor.putString("petName", petName);
            editor.putString("petUsername", username);
            editor.putString("petType", petType);
            editor.commit();
        }
        createPetResponse.createPetFinish(responseValue);
    }

}
