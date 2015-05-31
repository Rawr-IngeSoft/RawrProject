package com.example.david.rawr.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.rawr.Interfaces.AnswerRequestResponse;
import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.SQLite.SQLiteHelper;
import com.example.david.rawr.Tasks.AnswerRequest;
import com.example.david.rawr.Tasks.GetPhoto;


public class FriendRequest_fragment extends Fragment implements GetPhotoResponse {

    String petNameText, petTypeText, petRaceText,  petBirthayText, petPicture, petUsername;
    String petGenderText, ownerNameText, ownerLastnameText, ownerPicture, ownerUsername;
    String myUsername;
    ImageView petPictureIV, ownerPictureIV;
    Bitmap petBitmap, ownerBitmap;
    SQLiteHelper sqLiteHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();
        sqLiteHelper = new SQLiteHelper(getActivity());
        /** Getting integer data of the key current_page from the bundle */
        myUsername = data.getString("myUsername");
        petNameText = data.getString("petName");
        petTypeText = data.getString("petType");
        petBirthayText = data.getString("petBirthday");
        petPicture = data.getString("petPicture");
        petUsername = data.getString("petUsername");
        petRaceText = data.getString("petRace");
        petGenderText = data.getString("petGender");
        ownerNameText = data.getString("ownerName");
        ownerLastnameText = data.getString("ownerLastname");
        ownerPicture = data.getString("ownerPicture");
        ownerUsername = data.getString("ownerUsername");
        Log.e(petPicture, petUsername);
        if (petPicture.compareTo("null") != 0){
            GetPhoto getPhoto = new GetPhoto(petPicture, petUsername, this);
            getPhoto.execute();
        }else if (ownerPicture.compareTo("null") != 0){
            petBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_profilepicture_male);
            GetPhoto getPhoto = new GetPhoto(ownerPicture, ownerUsername, this);
            getPhoto.execute();
        }else{
            petBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_profilepicture_male);
            ownerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_profilepicture_male);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friend_request, container, false);
        TextView petName = (TextView) v.findViewById(R.id.friend_request_petName);
        TextView petType = (TextView) v.findViewById(R.id.friend_request_petType);
        TextView petBirthday = (TextView) v.findViewById(R.id.friend_request_petBirthday);
        TextView petGender = (TextView) v.findViewById(R.id.friend_request_petGender);
        TextView petRace = (TextView) v.findViewById(R.id.friend_request_petRace);
        TextView ownerName = (TextView) v.findViewById(R.id.friend_request_ownerName);
        TextView ownerLastname = (TextView) v.findViewById(R.id.friend_request_ownerLastname);
        petPictureIV = (ImageView) v.findViewById(R.id.friend_request_petPicture);
        ownerPictureIV = (ImageView) v.findViewById(R.id.friend_request_ownerPicture);

        petName.setText(petNameText);
        petType.setText(petTypeText);
        petBirthday.setText(petBirthayText);
        petGender.setText(petGenderText);
        petRace.setText(petRaceText);
        ownerName.setText(ownerNameText);
        ownerLastname.setText(ownerLastnameText);
        petPictureIV.setImageBitmap(petBitmap);
        ownerPictureIV.setImageBitmap(ownerBitmap);

        ImageView acceptButton = (ImageView) v.findViewById(R.id.fragment_friend_request_accept);
        ImageView declineButton = (ImageView) v.findViewById(R.id.fragment_friend_request_decline);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswerRequest answerRequest = new AnswerRequest(petUsername, myUsername, "accepted");
                answerRequest.execute();
                sqLiteHelper.answerRequest(petUsername);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswerRequest answerRequest = new AnswerRequest(petUsername, myUsername, "decline");
                answerRequest.execute();
                sqLiteHelper.answerRequest(petUsername);
            }
        });
        return v;
    }

    @Override
    public void getPhotoFinish(Bitmap bitmap) {
        if(petBitmap == null) {
            petBitmap = bitmap;
            petPictureIV.setImageBitmap(petBitmap);
            if (ownerPicture.compareTo("null") != 0) {
                GetPhoto getPhoto = new GetPhoto(ownerPicture, ownerUsername, this);
                getPhoto.execute();
            }else{
                ownerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_profilepicture_male);
                ownerPictureIV.setImageBitmap(ownerBitmap);
            }
        }else{
            ownerBitmap = bitmap;
            ownerPictureIV.setImageBitmap(ownerBitmap);
        }
    }
}
