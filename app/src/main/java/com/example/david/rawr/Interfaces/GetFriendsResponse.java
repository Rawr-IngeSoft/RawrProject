package com.example.david.rawr.Interfaces;

import com.example.david.rawr.Models.Friend;
import com.example.david.rawr.Models.Pet;

import java.util.ArrayList;

/**
 * Created by david on 11/05/2015.
 */
public interface GetFriendsResponse {
    void getFriendsFinish(ArrayList<Friend> output);
}
