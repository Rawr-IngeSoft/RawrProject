package com.example.david.rawr.db;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;





public class DbMethods {

    public static String username;


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
        DbMethods.username = null;
        ValidateUser validate = new ValidateUser(username, password);
        validate.execute();

        while(validate.getStatus() != AsyncTask.Status.FINISHED){}
        if (DbMethods.username.compareTo("-1") != 0)
            return false;
        else
            return true;
    }

    public static class ValidateUser extends AsyncTask<String, String, String> {

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
            try {
                post.setEntity(new UrlEncodedFormEntity(params));
                response = client.execute(post);
                Header header = response.getFirstHeader("Status");
                Log.i("hola:", header.getName());
                Log.i("hola:", header.getValue());
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
                String status = EntityUtils.toString(this.response.getEntity());
                Log.i("status ", status );

                if(status.equals("1")){
                    DbMethods.username = this.user;
                }else{
                    DbMethods.username = "-1";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
