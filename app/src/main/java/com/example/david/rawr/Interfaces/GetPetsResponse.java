package com.example.david.rawr.Interfaces;

import com.example.david.rawr.Models.Pet;
import com.example.david.rawr.Models.Post;

import java.util.ArrayList;

/**
 * Created by david on 11/05/2015.
 */
public interface GetPetsResponse {
    void getPetsFinish(ArrayList<Pet> output);
}
