package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.david.rawr.Adapters.ConfigurationListAdapter;
import com.example.david.rawr.R;
import com.facebook.login.LoginManager;


public class Configuration_screen extends Activity  {

    ListView list;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_screen);
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        list = (ListView)findViewById(R.id.configuration_screen_list);
        list.setAdapter(new ConfigurationListAdapter(this));
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position", String.valueOf(position));
                if (position == 5) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("username");
                    editor.remove("name");
                    editor.remove("lastName");
                    editor.remove("pictureUri");
                    editor.commit();
                    if(LoginManager.getInstance() != null) {
                        LoginManager.getInstance().logOut();
                    }
                    Intent intent = new Intent(Configuration_screen.this, LogIn_screen.class );
                    startActivity(intent);
                    Configuration_screen.this.finish();
                }
            }
        });
    }

}
