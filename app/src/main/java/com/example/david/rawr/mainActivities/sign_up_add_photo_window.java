package com.example.david.rawr.mainActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.david.rawr.R;

import java.io.FileNotFoundException;


public class sign_up_add_photo_window extends Activity implements View.OnClickListener {

    Button selectButton, nextButton;
    ImageView photoImageView;
    Bitmap bitmap = null;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_add_photo_window);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            username = bundle.getString("username");
            Log.i("username:", username);
        }
        selectButton = (Button)findViewById(R.id.selectImage);
        nextButton = (Button)findViewById(R.id.next);
        photoImageView = (ImageView)findViewById(R.id.signUp_imageView);
        selectButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_add_photo_window, menu);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case(R.id.selectImage):
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            break;
            case(R.id.next):
                if(bitmap != null){
                    //TODO
                    // consume service to add photo to owner
                    intent = new Intent(sign_up_add_photo_window.this, Loading_screen.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    Toast.makeText(this, "Sign up", Toast.LENGTH_LONG).show();
                }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                photoImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Error loading the photo", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
