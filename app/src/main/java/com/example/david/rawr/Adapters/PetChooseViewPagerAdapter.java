package com.example.david.rawr.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.david.rawr.SQLite.PetSQLiteHelper;
import com.example.david.rawr.Models.Pet;
import com.example.david.rawr.Fragments.ChoosePetFragment;
import com.example.david.rawr.Fragments.CreatePet_fragment;

import java.util.ArrayList;

/**
 * Created by david on 27/04/2015.
 */
public class PetChooseViewPagerAdapter extends FragmentPagerAdapter{

    private ArrayList<Pet> petList;

    public PetChooseViewPagerAdapter(android.support.v4.app.FragmentManager fm, Context context){
        super(fm);
        PetSQLiteHelper petSQLiteHelper = new PetSQLiteHelper(context);
        petList = petSQLiteHelper.getPets();
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

    public ArrayList<Pet> getPetList() {
        return petList;
    }

    public void setPetList(ArrayList<Pet> petList) {
        this.petList = petList;
    }
}
