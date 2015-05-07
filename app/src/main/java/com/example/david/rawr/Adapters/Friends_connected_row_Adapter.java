package com.example.david.rawr.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.david.rawr.R;

import java.util.ArrayList;

/**
 * Created by Estudiante on 05/05/2015.
 */
public class Friends_connected_row_Adapter extends BaseAdapter {

    Context context;
    ArrayList<String> petNames;
    private static LayoutInflater inflater;

    public Friends_connected_row_Adapter(Context context, ArrayList<String> petNames) {
        this.petNames = petNames;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return petNames.size();
    }

    @Override
    public Object getItem(int position) {
        return petNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.friends_connected_row, null);
            TextView petName = (TextView)convertView.findViewById(R.id.friends_connected_row_petName);
            petName.setText(petNames.get(position));
        }
        return convertView;
    }

    public ArrayList<String> getPetNames() {
        return petNames;
    }

    public void setPetNames(ArrayList<String> petNames) {
        this.petNames = petNames;
    }
}
