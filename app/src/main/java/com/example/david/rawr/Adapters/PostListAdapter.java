package com.example.david.rawr.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.rawr.Interfaces.CreatePostResponse;
import com.example.david.rawr.Interfaces.GetPhotoResponse;
import com.example.david.rawr.Interfaces.GetPostsResponse;
import com.example.david.rawr.Interfaces.UploadPhotoResponse;
import com.example.david.rawr.R;
import com.example.david.rawr.Models.Post;
import com.example.david.rawr.Tasks.CreatePost;
import com.example.david.rawr.Tasks.GetPhoto;
import com.example.david.rawr.Tasks.GetPosts;
import com.example.david.rawr.Tasks.UploadPhoto;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by david on 24/04/2015.
 */
public class PostListAdapter extends BaseAdapter implements UploadPhotoResponse,CreatePostResponse, GetPostsResponse{

    Context context;
    ArrayList<Post> data;
    ImageView postPhoto;
    Uri postPhotoUri = null;
    Bitmap postPhotoBitmap = null;
    EditText textPost;
    private static LayoutInflater inflater;

    public PostListAdapter(Context context, ArrayList<Post> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size()+1;
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
    public void uploadFinish(ArrayList<String> response) {
        if(response.get(0).compareTo("1") == 0) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            CreatePost createPost = new CreatePost(sharedPreferences.getString("petUsername", ""), "post", response.get(2), textPost.getText().toString(), this, context);
            createPost.execute();
        }else{
            Log.e("status", "Error uploading the photo");
        }
    }

    @Override
    public void createPostFinish(String output) {
        postPhoto.setImageBitmap(null);
        textPost.setText("");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = 0;
        params.height = 0;
        postPhoto.setLayoutParams(params);
        notifyDataSetChanged();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        GetPosts getPosts = new GetPosts(sharedPreferences.getString("petUsername", ""), this);
        getPosts.execute();
    }

    @Override
    public void getPostsFinish(ArrayList<Post> output) {
        data = output;
        notifyDataSetChanged();
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
        int viewType = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            if(viewType == 0) {
                convertView = inflater.inflate(R.layout.create_post_row, null);
                textPost = (EditText)convertView.findViewById(R.id.newsfeed_post_text);
            }else {
                convertView = inflater.inflate(R.layout.post_row, null);
                holder.petIdTv = (TextView) convertView.findViewById(R.id.post_petId);
                holder.textTv = (TextView) convertView.findViewById(R.id.post_text);
                holder.dateTv = (TextView) convertView.findViewById(R.id.post_date);
                holder.postPicture = (ImageView) convertView.findViewById(R.id.post_postPhoto);
                holder.senderPicture = (ImageView) convertView.findViewById(R.id.post_senderPicture);
                convertView.setTag(holder);
            }

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (viewType == 1) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (data.get(position - 1).getPhotoBitmap() == null) {
                params.width = 0;
                params.height = 0;
                holder.postPicture.setLayoutParams(params);
            } else {
                params.width = LinearLayout.LayoutParams.FILL_PARENT;
                params.height = (int) context.getResources().getDimension(R.dimen.post_postPicture_height);
                params.bottomMargin = 20;
                holder.postPicture.setLayoutParams(params);
                holder.postPicture.setImageBitmap(data.get(position - 1).getPhotoBitmap());
            }
            Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/calibri.ttf");
            holder.petIdTv.setTypeface(type);
            holder.textTv.setTypeface(type);
            holder.dateTv.setTypeface(type);
            holder.petIdTv.setText(data.get(position - 1).getPetUsername());
            holder.textTv.setText(data.get(position - 1).getText());
            holder.dateTv.setText(data.get(position - 1).getDate());
            holder.senderPicture.setImageBitmap(data.get(position - 1).getSenderPhotoBitmap());
        }else{
            Button postButton = (Button)convertView.findViewById(R.id.newsfeed_post_button);
            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    if (sharedPreferences.contains("petUsername")) {
                        if (postPhotoBitmap != null) {
                            UploadPhoto uploadPhoto = new UploadPhoto(postPhotoBitmap, sharedPreferences.getString("petUsername", ""), PostListAdapter.this);
                            uploadPhoto.execute();
                        }else{
                            if (textPost.getText().toString().compareTo("") != 0) {
                                CreatePost createPost = new CreatePost(sharedPreferences.getString("petUsername", ""), "post", "null", textPost.getText().toString(), PostListAdapter.this, context);
                                createPost.execute();
                            }
                        }
                    }else{
                        Toast.makeText(context, "First... Create you pet", Toast.LENGTH_LONG).show();
                    }
                }
            });
            postPhoto = (ImageView)convertView.findViewById(R.id.newsfeed_post_UploadPhoto);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (postPhotoUri != null) {
                params.width = LinearLayout.LayoutParams.FILL_PARENT;
                params.height = (int) context.getResources().getDimension(R.dimen.post_postPicture_height);
                try {
                    postPhotoBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(postPhotoUri));
                    postPhoto.setImageBitmap(postPhotoBitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(context, "Error loading the photo", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }else{
                params.width = 0;
                params.height = 0;
            }
            postPhoto.setLayoutParams(params);
            ImageView uploadPhotoButton = (ImageView)convertView.findViewById(R.id.newsfeed_post_uploadPhoto_button);
            uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ((Activity) context).startActivityForResult(intent, 0);
                }
            });
        }
        return convertView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            postPhotoUri = data.getData();
            notifyDataSetChanged();
        }
    }

    public void setData(ArrayList<Post> data) {
        this.data = data;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? 0 : 1;
    }
}
