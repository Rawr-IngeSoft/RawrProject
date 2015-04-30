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
 * Created by david on 27/04/2015.
 */
public class ConfigurationListAdapter extends BaseAdapter{

    Context context;
    private static LayoutInflater inflater;
    private ArrayList<String> configurationItems;
    public ConfigurationListAdapter(Context context) {
        this.context = context;
        configurationItems = new ArrayList<>();
        configurationItems.add("Perfil");
        configurationItems.add("Editar");
        configurationItems.add("Mover");
        configurationItems.add("Subir");
        configurationItems.add("Bajar");
        configurationItems.add("Log Out");
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return configurationItems.size();
    }

    @Override
    public Object getItem(int position) {
        return configurationItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.configuration_row, null);
            TextView configurationItem = (TextView) convertView.findViewById(R.id.configuration_row_textView);
            configurationItem.setText(configurationItems.get(position));
        }
        return convertView;
    }


}
