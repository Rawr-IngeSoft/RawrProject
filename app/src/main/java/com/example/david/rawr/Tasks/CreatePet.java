package com.example.david.rawr.Tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.david.rawr.Interfaces.CreatePetResponse;
import com.example.david.rawr.SQLite.SQLiteHelper;
import com.example.david.rawr.Models.Pet;

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
 * Created by David on 15/04/2015.
 */
// REQ-014
public class CreatePet extends AsyncTask<String, Integer, String> {

    private final String username;
    private final String petName;
    private final String petType;
    private final String owner;
    private final String petGender;
    private static String url_create_pet = "http://178.62.233.249/rawr/create_pet.php";
    private CreatePetResponse createPetResponse;
    private SharedPreferences sharedPreferences;
    private Context context;
    public CreatePet(String username, String petName, String petType, String owner, String petGender, CreatePetResponse createPetResponse, Context context) {
        this.username = username;
        this.petName = petName;
        this.petType = petType;
        this.owner = owner;
        this.createPetResponse = createPetResponse;
        this.context = context;
        this.petGender = petGender;
        sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
    }

    protected String doInBackground(String... args) {
        String status = "0";
        try {
            // Building Parameters
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", this.username);
            jsonObject.put("name", this.petName);
            jsonObject.put("type", this.petType);
            jsonObject.put("owner_username", this.owner);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url_create_pet);
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
        if (responseValue.compareTo("1") == 0){
            // Focus actual pet
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =  sharedPreferences.edit();
            editor.putString("petName", petName);
            editor.putString("petUsername", username);
            editor.putString("petType", petType);
            editor.putString("petGender", petGender);
            editor.commit();

            // Save pet in sqlite
            SQLiteHelper SQLiteHelper = new SQLiteHelper(context);
            SQLiteHelper.addPet(new Pet(username,petName,petType,"","", petGender));
        }
        createPetResponse.createPetFinish(responseValue);
    }

}
