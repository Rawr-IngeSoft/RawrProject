package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.david.rawr.Adapters.ConfigurationListAdapter;
import com.example.david.rawr.R;
import com.example.david.rawr.SQLite.SQLiteHelper;
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
        /*
        * @Requirements REQ-009
         */
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Listener de eventos de click de items de la lista
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // En el caso de que sea la opcion de logout se borran todos los datos de la base
                // de datos local y se redirige a la pagina de LogIn_screen
                if (position == 5) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    SQLiteHelper SQLiteHelper = new SQLiteHelper(Configuration_screen.this);
                    SQLiteHelper.clearDB();
                    editor.clear();
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

    public void onBackPressed() {
        Intent intent = new Intent(this, Owner_Profile_screen.class);
        startActivity(intent);
        this.finish();
    }
}
