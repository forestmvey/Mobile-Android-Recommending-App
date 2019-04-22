package com.example.mobileappsproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONException;


/**
 * fragment class to display the search page to retrieve json data from api
 */
public class SearchFragment extends Fragment {
    private Button searchButton;
    private CheckBox movieCheck;
    private CheckBox musicCheck;
    private CheckBox bookCheck;
    private CheckBox authorCheck;
    private CheckBox showCheck;
    private CheckBox podcastCheck;
    private CheckBox gameCheck;
    private TextView searchName;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_search, container, false);
        searchButton = (Button) v.findViewById(R.id.searchButton);
        movieCheck = (CheckBox)v.findViewById(R.id.moviesCheckBox);
        musicCheck = (CheckBox) v.findViewById(R.id.musicCheckBox);
        bookCheck = (CheckBox) v.findViewById(R.id.booksCheckBox);
        authorCheck = (CheckBox) v.findViewById(R.id.authorsCheckBox);
        showCheck = (CheckBox) v.findViewById(R.id.showsCheckBox);
        podcastCheck = (CheckBox) v.findViewById(R.id.podcastsCheckBox);
        gameCheck = (CheckBox) v.findViewById(R.id.gamesCheckBox);
        searchName = (TextView) v.findViewById(R.id.recommendationText);

        /**
         * when checkbox clicked set the URL parameter
         */
        movieCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTypesCheckList("typeMovie", movieCheck);
            }
        });
        musicCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTypesCheckList("typeMusic", musicCheck);
            }
        });
        bookCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTypesCheckList("typeBook", bookCheck);
            }
        });
        authorCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTypesCheckList("typeAuthor", authorCheck);
            }
        });
        showCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTypesCheckList("typeShow", showCheck);
            }
        });
        podcastCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTypesCheckList("typePodcast", podcastCheck);
            }
        });
        gameCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTypesCheckList("typeGame", gameCheck);
            }
        });


        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ((MainActivity) getActivity()).setSearchName(searchName.getText().toString());
               }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("search clicked");
                try {
                    ((MainActivity)getActivity()).getJSON();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    /**
     * Set the URL parameter when checkbox checked/unchecked for GET request
     * @param toSet - string to add to search url
     * @param box - box being checked
     */
    private void setTypesCheckList(String toSet, CheckBox box){
        ((MainActivity)getActivity()).setTypeList(toSet, (box.isChecked()));
    }
}
