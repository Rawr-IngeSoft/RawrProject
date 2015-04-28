package com.example.david.rawr.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.david.rawr.models.Pet;
import com.example.david.rawr.otherClasses.ChoosePetFragment;

import java.util.ArrayList;

/**
 * Created by david on 27/04/2015.
 */
public class PetChooseViewPagerAdapter extends FragmentPagerAdapter{

    ArrayList<Pet> petList;
    public PetChooseViewPagerAdapter(android.support.v4.app.FragmentManager fm, ArrayList<Pet> petList){
        super(fm);
        this.petList = petList;
    }
    @Override
    public Fragment getItem(int i) {
        ChoosePetFragment choosePetFragment = new ChoosePetFragment();
        Bundle data = new Bundle();
        Pet pet = petList.get(i);
        data.putString("petName", pet.getPetName());
        data.putString("petType", pet.getPetType());
        data.putString("petBirthday", pet.getPetBirthday());
        data.putString("pictureUri",pet.getPetPictureUri());
        choosePetFragment.setArguments(data);
        return choosePetFragment;
    }

    @Override
    public int getCount() {
        return petList.size();
    }
}
