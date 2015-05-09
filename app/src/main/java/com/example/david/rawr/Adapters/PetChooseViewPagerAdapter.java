package com.example.david.rawr.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.david.rawr.MainActivities.Owner_Profile_screen;
import com.example.david.rawr.R;
import com.example.david.rawr.models.Pet;
import com.example.david.rawr.otherClasses.ChoosePetFragment;
import com.example.david.rawr.otherClasses.CreatePet_fragment;

import java.util.ArrayList;

/**
 * Created by david on 27/04/2015.
 */
public class PetChooseViewPagerAdapter extends FragmentPagerAdapter{

    ArrayList<Pet> petList;
    Owner_Profile_screen owner_profile_screen;
    public PetChooseViewPagerAdapter(android.support.v4.app.FragmentManager fm, ArrayList<Pet> petList, Owner_Profile_screen owner_profile_screen){
        super(fm);
        this.petList = petList;
        this.owner_profile_screen = owner_profile_screen;
    }
    @Override
    public Fragment getItem(int i) {
        if (i < petList.size()) {
            ChoosePetFragment choosePetFragment = new ChoosePetFragment();
            Bundle data = new Bundle();
            Pet pet = petList.get(i);
            data.putString("petName", pet.getPetName());
            data.putString("petType", pet.getPetType());
            data.putString("petBirthday", pet.getPetBirthday());
            data.putString("pictureUri", pet.getPetPictureUri());
            choosePetFragment.setArguments(data);
            return choosePetFragment;
        }else{
            CreatePet_fragment createPet_fragment = new CreatePet_fragment();
            return createPet_fragment;
        }
    }

    @Override
    public int getCount() {
        return petList.size()+1;
    }
}
