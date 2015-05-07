package com.example.david.rawr.otherClasses;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.rawr.MainActivities.Owner_Profile_screen;
import com.example.david.rawr.R;
import com.example.david.rawr.db.GetPhoto;

import java.util.concurrent.ExecutionException;


public class CreatePet_fragment extends android.support.v4.app.Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_pet_fragment, container,false);
        return v;
    }
}
