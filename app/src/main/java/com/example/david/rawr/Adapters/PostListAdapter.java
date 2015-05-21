package com.example.david.rawr.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.Models.Post;
import com.example.david.rawr.Tasks.GetPhoto;

import java.util.ArrayList;

/**
 * Created by david on 24/04/2015.
 */
public class PostListAdapter extends BaseAdapter implements GetPhotoResponse{

    Context context;
    ArrayList<Post> data;
    ImageView postPicture;
    private static LayoutInflater inflater;

    public PostListAdapter(Context context, ArrayList<Post> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/calibri.ttf");
            convertView = inflater.inflate(R.layout.post_row, null);
            TextView petIdTv = (TextView)convertView.findViewById(R.id.post_petId);
            TextView textTv = (TextView)convertView.findViewById(R.id.post_text);
            TextView dateTv =(TextView)convertView.findViewById(R.id.post_date);
            postPicture = (ImageView)convertView.findViewById(R.id.post_postPhoto);
            petIdTv.setTypeface(type);
            textTv.setTypeface(type);
            dateTv.setTypeface(type);
            petIdTv.setText(data.get(position).getPetUsername());
            textTv.setText(data.get(position).getText());
            dateTv.setText(data.get(position).getDate());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(data.get(position).getPhoto().compareTo("null") == 0){
                params.width=0;
                params.height=0;
                postPicture.setLayoutParams(params);
            }else{
                GetPhoto getPhoto = new GetPhoto( "5543aef35c496.jpg","disney", this);
                getPhoto.execute();
            }
        }
        return convertView;
    }

    public void setData(ArrayList<Post> data) {
        this.data = data;
    }

    @Override
    public void getPhotoFinish(Bitmap bitmap) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = LinearLayout.LayoutParams.FILL_PARENT;
        params.height = (int) context.getResources().getDimension(R.dimen.post_profile_picture);
        params.bottomMargin = 20;
        postPicture.setLayoutParams(params);
        Log.e("bitmap", bitmap.toString());
        postPicture.setImageBitmap(bitmap);
    }
}
