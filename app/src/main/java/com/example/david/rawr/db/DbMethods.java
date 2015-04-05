package com.example.david.rawr.db;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;





public class DbMethods {

    private static String username;


    private static String url_create_user = "http://178.62.233.249/rawr/create_user.php";
    //private static String url_create_user = "http://localhost/rawr/create_user.php";

    private static String url_validate_user = "http://178.62.233.249/rawr/validate_user.php";

    /*public DbMethods(Context context){
        this.context = context;
        dbManager = new DbManager(context);
    }*/

    public static void createUserOwner(String username, String password, String name, String last){
        new CreateUserOwnerDB(username, password, name, last).execute();
    }

    private static class CreateUserOwnerDB extends AsyncTask<String, String, String> {

        private String user;
        private String pass;
        private String name;
        private String last;

        CreateUserOwnerDB(String user, String pass, String name, String last){
            this.user = user;
            this.pass = pass;
            this.name = name;
            this.last = last;
        }

        /**
         * Creating UserOwner
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", this.user));
            params.add(new BasicNameValuePair("password", this.pass));
            params.add(new BasicNameValuePair("name", this.name));
            params.add(new BasicNameValuePair("lastname", this.last));

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url_create_user);

            try {
                post.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse response = client.execute(post);
                Log.i("response ",response.getStatusLine().getStatusCode()+"");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }



    }

    public static boolean login(String username, String password){
       /* todo */
        new ValidateUser(username, password).execute();
       return true;
    }

    private static class ValidateUser extends AsyncTask<String, String, String> {

        private String user;
        private String pass;
        private HttpResponse response;

        ValidateUser(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }

        /**
         * Validating user
         */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", this.user));
            params.add(new BasicNameValuePair("password", this.pass));

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url_validate_user);
            Log.i("----------como vy","voy bien");
            try {
                post.setEntity(new UrlEncodedFormEntity(params));
                response = client.execute(post);


                Log.i("response ", response.getStatusLine().getStatusCode() + "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task
         * *
         */
        protected void onPostExecute(String file_url) {
            try {
                HttpEntity entity = this.response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                Log.i("-----------result", result + "");
                JSONObject jObject = new JSONObject(result);
                String status = jObject.getString("status");

                Log.i("status", status + "");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
