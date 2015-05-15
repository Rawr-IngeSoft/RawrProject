package com.example.david.rawr.Fragments;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.david.rawr.Adapters.PetChooseViewPagerAdapter;
import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.MainActivities.Owner_Profile_screen;
import com.example.david.rawr.R;
import com.example.david.rawr.SQLite.SQLiteHelper;
import com.example.david.rawr.Tasks.GetPhoto;
import com.example.david.rawr.otherClasses.RoundImage;

import java.util.concurrent.ExecutionException;


public class ChoosePetFragment extends android.support.v4.app.Fragment implements GetPhotoResponse, View.OnClickListener {

    String petNameText, petTypeText, petBirthayText, pictureUri, petUsername;
    boolean selected;
    FrameLayout frameContainer;
    ImageView petPicture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        /** Getting integer data of the key current_page from the bundle */
        petNameText = data.getString("petName", "");
        petTypeText = data.getString("petType", "");
        petBirthayText = data.getString("petBirthday", "");
        pictureUri = data.getString("pictureUri","");
        petUsername = data.getString("petUsername");
        selected = data.getBoolean("selected");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_pet, container,false);
        TextView petName = (TextView) v.findViewById(R.id.choosePetName);
        TextView petType = (TextView) v.findViewById(R.id.choosePetType);
        TextView petBirthday = (TextView) v.findViewById(R.id.choosePetBirthday);
        petPicture = (ImageView)v.findViewById(R.id.choosePetImageView);
        frameContainer = (FrameLayout)v.findViewById(R.id.fragment_choose_pet_container);
        if(selected){
            frameContainer.setBackgroundColor(Color.parseColor("#c6f274"));
        }else{
            frameContainer.setBackgroundColor(Color.parseColor("#c5e0d2"));
        }
        GetPhoto getPhoto = new GetPhoto(pictureUri, petUsername, this);
        getPhoto.execute();
        petName.setText(petNameText);
        petType.setText(petTypeText);
        petBirthday.setText(petBirthayText);
        frameContainer.setOnClickListener(this);
        return v;
    }

    @Override
    public void getPhotoFinish(Bitmap bitmap) {
        if (bitmap != null) {
            petPicture.setImageBitmap(RoundImage.getRoundedShape(bitmap));
        }else{
            //petPicture.setImageBitmap(RoundImage.getRoundedShape(BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_picture_female)));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_choose_pet_container:
                Owner_Profile_screen owner_profile_screen = (Owner_Profile_screen)getActivity();
                owner_profile_screen.update(petUsername);
                break;
        }
    }
}
