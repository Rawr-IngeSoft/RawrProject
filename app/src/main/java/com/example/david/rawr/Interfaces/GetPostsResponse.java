package com.example.david.rawr.Interfaces;

import com.example.david.rawr.models.Post;

import java.util.ArrayList;

/**
 * Created by david on 24/04/2015.
 */
public interface GetPostsResponse {
    void getPostsFinish(ArrayList<Post> output);
}
