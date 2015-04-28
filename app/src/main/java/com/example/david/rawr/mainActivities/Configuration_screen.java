package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.david.rawr.Adapters.ConfigurationListAdapter;
import com.example.david.rawr.R;


public class Configuration_screen extends Activity {

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_screen);
        list = (ListView)findViewById(R.id.configuration_screen_list);
        list.setAdapter(new ConfigurationListAdapter(this));
    }


}
