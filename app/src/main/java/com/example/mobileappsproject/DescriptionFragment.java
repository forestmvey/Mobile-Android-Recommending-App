package com.example.mobileappsproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DescriptionFragment extends Fragment {
    public TextView theView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_description, container, false);
        TextView name = (TextView)v.findViewById(R.id.descriptionName);
        TextView type = (TextView)v.findViewById(R.id.descriptionType);
        TextView description = (TextView)v.findViewById(R.id.descriptionDescription);
        TextView wiki = (TextView)v.findViewById(R.id.descriptionWiki);
        TextView youtube = (TextView)v.findViewById(R.id.descriptionYoutube);

        name.setText(getArguments().getString("name"));
        type.setText(getArguments().getString("type"));
        description.setText(getArguments().getString("description"));
        wiki.setText(getArguments().getString("wiki"));
        youtube.setText(getArguments().getString("youtube"));

        return v;
    }


    public static DescriptionFragment newInstance(String name, String type, String description,String wiki, String youtube) {
        DescriptionFragment frag = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("type", type);
        args.putString("description", description);
        args.putString("wiki", wiki);
        args.putString("youtube", youtube);
        frag.setArguments(args);
        return frag;
    }

}
