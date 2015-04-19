package com.example.david.rawr.mainActivities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.rawr.R;
import com.example.david.rawr.db.CreatePet;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by david on 07/04/15.
 */
public class CreatePet_window extends Activity{

    Button createPet;
    EditText petName;
    ListView typeList;
    String petType = null, username = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pet_window);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            username = bundle.getString("username");
            Log.i("username:", username);
        }
        createPet = (Button)findViewById(R.id.createPet);
        petName = (EditText)findViewById(R.id.petName);
        typeList = (ListView)findViewById(R.id.list);
        typeList.setClickable(true);
        ArrayList<String> types =  new ArrayList<>();
        types.add("Dog"); types.add("Cat"); types.add("Horse"); types.add("Ant"); types.add("Panda");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.string_list_item,R.id.stringListItem, types);
        typeList.setAdapter(adapter);
        typeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView listItem;
                for (int i = 0; i < typeList.getChildCount(); i++){
                    listItem = (TextView)typeList.getChildAt(i).findViewById(R.id.stringListItem);
                    listItem.setTextColor(Color.rgb(42, 42, 42));
                }
                listItem = (TextView)typeList.getChildAt(position).findViewById(R.id.stringListItem);
                listItem.setTextColor(Color.GREEN);
                petType = (String)typeList.getItemAtPosition(position);
            }
        });

        createPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String petNameText = petName.getText().toString();
                if(username == null || petType == null || petNameText.compareTo("") == 0){
                    Toast.makeText(getApplicationContext(), "Invalid parameters", Toast.LENGTH_SHORT).show();
                }else{
                    //TODO
                    // ask for PHP service
                    CreatePet createPet = new CreatePet(petNameText, petType, username);
                    try {
                        String responseValue = createPet.execute().get();
                        if (responseValue.compareTo("1") == 0){
                            Toast.makeText(getApplicationContext(), "Error Creating Pet", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Pet Created", Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_pet_window, menu);
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
