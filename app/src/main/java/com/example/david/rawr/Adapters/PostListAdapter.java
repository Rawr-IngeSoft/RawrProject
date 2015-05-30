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
import java.util.concurrent.ExecutionException;

/**
 * Created by david on 24/04/2015.
 */
public class PostListAdapter extends BaseAdapter{

    Context context;
    ArrayList<Post> data;
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

    static class ViewHolder{
        TextView petIdTv;
        TextView textTv;
        TextView dateTv;
        ImageView postPicture;
        ImageView senderPicture;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.post_row, null);
            holder.petIdTv = (TextView)convertView.findViewById(R.id.post_petId);
            holder.textTv = (TextView)convertView.findViewById(R.id.post_text);
            holder.dateTv =(TextView)convertView.findViewById(R.id.post_date);
            holder.postPicture = (ImageView)convertView.findViewById(R.id.post_postPhoto);
            holder.senderPicture = (ImageView)convertView.findViewById(R.id.post_senderPicture);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if(data.get(position).getPhotoBitmap() == null){
            params.width=0;
            params.height=0;
            holder.postPicture.setLayoutParams(params);
        }else{
            params.width = LinearLayout.LayoutParams.FILL_PARENT;
            params.height = (int) context.getResources().getDimension(R.dimen.post_postPicture_height);
            params.bottomMargin = 20;
            holder.postPicture.setLayoutParams(params);
            holder.postPicture.setImageBitmap(data.get(position).getPhotoBitmap());
        }
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/calibri.ttf");
        holder.petIdTv.setTypeface(type);
        holder.textTv.setTypeface(type);
        holder.dateTv.setTypeface(type);
        holder.petIdTv.setText(data.get(position).getPetUsername());
        holder.textTv.setText(data.get(position).getText());
        holder.dateTv.setText(data.get(position).getDate());
        holder.senderPicture.setImageBitmap(data.get(position).getSenderPhotoBitmap());
        return convertView;
    }

    public void setData(ArrayList<Post> data) {
        this.data = data;
    }

}
