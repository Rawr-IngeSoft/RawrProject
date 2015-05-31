package com.example.david.rawr.Tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.david.rawr.Interfaces.UploadPhotoResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by david on 26/04/2015.
 */
public class UploadPhoto extends AsyncTask<String, Integer, String> {

    Bitmap bitmap;
    String username, pictureUri, pictureId;
    UploadPhotoResponse uploadPhotoResponse;
    private static String url_upload_photo = "http://178.62.233.249/rawr/image_upload.php";

    public UploadPhoto(Bitmap bitmap, String username, UploadPhotoResponse uploadPhotoResponse) {
        this.bitmap = bitmap;
        this.username = username;
        this.uploadPhotoResponse = uploadPhotoResponse;
    }

    @Override
    protected String doInBackground(String... params) {
        JSONObject jsonObjSend = new JSONObject();
        try {
            jsonObjSend.put("username", this.username);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            jsonObjSend.put("photo", encoded);
            jsonObjSend.put("extension", "jpg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url_upload_photo);

        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        JSONObject jsonResponse = null;
        String status = "0";
        try {
            StringEntity se = new StringEntity(jsonObjSend.toString());
            post.setEntity(se);

            HttpResponse response;
            response = client.execute(post);
            JsonParser jsonParser = new JsonParser(response.getEntity().getContent());

            jsonResponse= jsonParser.getjObject();
            Log.e("uploadImage", jsonResponse.toString());
            status = jsonResponse.getString("status");
            pictureUri = jsonResponse.getString("path");
            pictureId = jsonResponse.getString("id");
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
        ArrayList<String> response = new ArrayList<>();
        response.add(status);
        response.add(pictureUri);
        response.add(pictureId);
        uploadPhotoResponse.uploadFinish(response);
    }
}
