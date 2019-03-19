package com.example.mobileappsproject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class APIGetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_api);
        addSearchFragment();
    }
    private void addSearchFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SimpleFragment sampleFragment = new SimpleFragment();
        fragmentTransaction.add(R.id.fragment_container, sampleFragment);
        fragmentTransaction.commit();
    }
}
