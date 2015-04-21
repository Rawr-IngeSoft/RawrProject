package com.example.david.rawr.models;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;

/**
 * Created by Fabián Merchán on 21/04/2015.
 */
public abstract class PostAdapter extends ArrayAdapter<Post> {

    private Context context;
    private int resource;
    private List<Post> posts;

    Button button;

    public PostAdapter(Context context, int resource, List<Post> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.posts = objects;
    }

    public abstract void onEnter(Post post, View view);
}
