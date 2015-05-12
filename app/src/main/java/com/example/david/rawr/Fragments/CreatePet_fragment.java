package com.example.david.rawr.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.david.rawr.Interfaces.CreatePetResponse;
import com.example.david.rawr.MainActivities.CreatePet_add_photo_screen;
import com.example.david.rawr.R;
import com.example.david.rawr.Tasks.CreatePet;

import java.util.ArrayList;


public class CreatePet_fragment extends android.support.v4.app.Fragment implements CreatePetResponse {

    Button createPet;
    private EditText petName;
    private EditText petUsername;
    private RadioGroup gender;
    Spinner typeList;
    String username = null, genderString = "Female";
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
        gender = (RadioGroup)v.findViewById(R.id.createPet_fragment_gender_RG);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.createPet_fragment_gender_Female) {
                    genderString = "Female";
                } else{
                    genderString = "Male";
                }
            }
        });
        final ArrayList<String> types =  new ArrayList<>();
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
                String petNameText = petName.getText().toString();
                String petUsernameText = petUsername.getText().toString();
                String petType = typeList.getSelectedItem().toString();
                //TODO compare strings to regular expression
                if(username == null || petNameText.equals("") || petUsernameText.equals("")){
                    Toast.makeText(getActivity(), "Invalid parameters", Toast.LENGTH_SHORT).show();
                }else{
                    CreatePet createPet = new CreatePet(petUsernameText, petNameText, petType, username, genderString, CreatePet_fragment.this, getActivity());
                    createPet.execute();
                }
            }
        });
        return v;
    }

    @Override
    public void createPetFinish(String responseValue) {
        if (responseValue.compareTo("1") == 0){
            Toast.makeText(getActivity(), "Error Creating Pet", Toast.LENGTH_SHORT).show();
        }else{
            // TODO
            // Add photo to pet
            Toast.makeText(getActivity(), "Congrats!!! You have a new pet", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }
}
