package com.example.david.rawr.mainActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.david.rawr.R;
import com.example.david.rawr.db.CreateOwner;
import com.example.david.rawr.models.Post;
import com.example.david.rawr.models.PostAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by alfredo on 04/04/15.
 */
public class SignUp extends Activity implements View.OnClickListener {

    Button singUp;
    EditText userText, passText, nameText, lastnameText;
    AnimationDrawable loadingScreenAnimation;
    ImageView loadingScreen;
    ListView listView;
    PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up_window);
        singUp = (Button) findViewById(R.id.activity_sign_up_button_sign_up);
        userText = (EditText) findViewById(R.id.activity_sign_up_editText_username);
        passText = (EditText) findViewById(R.id.activity_sign_up_editText_password);
        nameText = (EditText) findViewById(R.id.activity_sign_up_editText_name);
        lastnameText = (EditText) findViewById(R.id.activity_sign_up_editText_lastname);
        listView = (ListView) findViewById(R.id.activity_sign_up_listView_test);

        setUpListView();

        loadingScreen = (ImageView) findViewById(R.id.activity_loading_screen_imageView_animationImageView);
        loadingScreen.setBackgroundResource(R.drawable.frame_animation);
        loadingScreenAnimation = (AnimationDrawable) loadingScreen.getBackground();
        loadingScreenAnimation.start();
        singUp.setOnClickListener(this);
    }

    private void setUpListView() {
        ArrayList<Post> posts = setUpArray(3);
        listView.setAdapter( new PostAdapter(this, R.layout.activity_sign_up_window, posts) {
            @Override
            public void onEnter(Post post, View view) {
                Button button = (Button)view.findViewById(R.id.test_item_button_button);
                button.setText(post.getNombreBoton());
            }
        });
    }

    private ArrayList<Post> setUpArray(int n) {
        ArrayList<Post> posts = new ArrayList<>();
        for ( int i=0; i<n; i++ ){
            posts.add(new Post(i+""));
        }
        return posts;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent;
        intent = new Intent(this, LogIn.class);
        startActivity(intent);
        finish_screen();
    }
    @Override
    public void onClick(View v) {
        String username = userText.getText().toString();
        String password = passText.getText().toString();
        String name = nameText.getText().toString();
        String lastname = lastnameText.getText().toString();
        loadingScreenAnimation.start();
        Toast.makeText(this, "Sign Up", Toast.LENGTH_LONG).show();
        CreateOwner createOwner = new CreateOwner(username, password, name, lastname);

        try {
            //TODO read response with json
            String status = createOwner.execute().get();
            Log.i("status--->", status);
            if (status.equals("1")){
                Toast.makeText(getApplicationContext(), "Owner Created", Toast.LENGTH_SHORT).show();
                Intent  intent = new Intent(SignUp.this, Loading_screen.class );

                startActivity(intent);
                finish_screen();

            }else{
                Toast.makeText(getApplicationContext(), "Error Creating Owner", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void finish_screen(){
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
