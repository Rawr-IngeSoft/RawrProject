package com.example.david.rawr.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.rawr.R;

public class Friends_row_fragment extends Fragment {
    TextView text;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.fragment_friends_row, container, false);
        text= (TextView) view.findViewById(R.id.detail);
        text.setText("hola");
        return view;
    }

}
