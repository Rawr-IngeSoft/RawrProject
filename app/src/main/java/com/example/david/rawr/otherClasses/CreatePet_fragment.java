package com.example.david.rawr.otherClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.david.rawr.R;

import java.util.ArrayList;


public class CreatePet_fragment extends android.support.v4.app.Fragment {

    Button createPet;
    private EditText petName;
    private EditText petUsername;
    Spinner typeList;
    String petType = null, username = null;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_pet, container,false);
        sharedPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("username")) {
            username = sharedPreferences.getString("username", "");
        }
        createPet = (Button)v.findViewById(R.id.createPet_fragment_create_button);
        petName = (EditText)v.findViewById(R.id.petName);
        petUsername = (EditText)v.findViewById(R.id.petUsername);
        typeList = (Spinner)v.findViewById(R.id.list);
        typeList.setClickable(true);
        ArrayList<String> types =  new ArrayList<>();
        types.add("Dog"); types.add("Cat"); types.add("Horse"); types.add("Ant"); types.add("Panda");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.pet_type_spinner_row, types);
        typeList.setAdapter(adapter);
        typeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        createPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }
}
