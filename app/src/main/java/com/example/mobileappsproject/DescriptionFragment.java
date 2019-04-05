package com.example.mobileappsproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;


public class DescriptionFragment extends Fragment {
    private TextView name;
    private TextView type;
    private TextView wiki;
    private TextView description;
    private TextView youtube;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_description, container, false);
        name = (TextView)v.findViewById(R.id.descriptionName);
        type = (TextView)v.findViewById(R.id.descriptionType);
        description = (TextView)v.findViewById(R.id.descriptionDescription);
        wiki = (TextView)v.findViewById(R.id.descriptionWiki);
        youtube = (TextView)v.findViewById(R.id.descriptionYoutube);
        Button saveItem = (Button)v.findViewById(R.id.addDescripButt);
        Button addOrRemove = (Button)v.findViewById(R.id.addDescripButt);

        name.setText(getArguments().getString("name"));
        type.setText(getArguments().getString("type"));
        description.setText(getArguments().getString("description"));
        wiki.setText(getArguments().getString("wiki"));
        youtube.setText(getArguments().getString("youtube"));
        if(!getArguments().getBoolean("addOrRemove")){
            addOrRemove.setText("Remove");
        }

        // Save or delete a JSON item to memory
        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        ((MainActivity) getActivity()).saveToJson(name.getText().toString(), type.getText().toString(), getArguments().getBoolean("addOrRemove"));
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    // Create a new instance to set arguments from retrieved json
    public static DescriptionFragment newInstance(String name, String type, String description,String wiki, String youtube, boolean addOrRemove) {
        DescriptionFragment frag = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("type", type);
        args.putString("description", description);
        args.putString("wiki", wiki);
        args.putString("youtube", youtube);
        args.putBoolean("addOrRemove", addOrRemove);
        frag.setArguments(args);
        return frag;
    }
}
