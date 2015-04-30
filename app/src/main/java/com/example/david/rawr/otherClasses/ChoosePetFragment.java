package com.example.david.rawr.otherClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.db.GetPhoto;

import java.util.concurrent.ExecutionException;


public class ChoosePetFragment extends android.support.v4.app.Fragment implements GetPhotoResponse {

    String petNameText, petTypeText, petBirthayText, pictureUri;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_pet, container,false);
        TextView petName = (TextView) v.findViewById(R.id.choosePetName);
        TextView petType = (TextView) v.findViewById(R.id.choosePetType);
        TextView petBirthday = (TextView) v.findViewById(R.id.choosePetBirthday);
        ImageView petPicture = (ImageView)v.findViewById(R.id.choosePetImageView);
        GetPhoto getPhoto = new GetPhoto(pictureUri, this);
        try {
            Bitmap bitmap = getPhoto.execute().get();
            if(bitmap != null)
                petPicture.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        petName.setText(petNameText);
        petType.setText(petTypeText);
        petBirthday.setText(petBirthayText);
        return v;
    }

    @Override
    public void getPhotoFinish(Bitmap bitmap) {

    }
}
