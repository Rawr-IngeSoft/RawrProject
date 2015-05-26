package com.example.david.rawr.Interfaces;

import com.example.david.rawr.Models.FriendRequest;

import java.util.ArrayList;

/**
 * Created by david on 21/05/2015.
 */
public interface GetFriendRequestsResponse {
    void getRequestsFinish(ArrayList<FriendRequest> output);
}
