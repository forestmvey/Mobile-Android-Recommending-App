package com.example.mobileappsproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SearchFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_search);
        }
        }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()) {
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
                break;
            case R.id.nav_video:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MovieFragment()).commit();
                break;
            case R.id.nav_music:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MusicFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
    return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen((GravityCompat.START))) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("C:\\Users\\fores\\AndroidStudioProjects\\MobileAppsProject2\\dummyJSON.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public void addSearchFragment() throws JSONException {

        JSONObject dummyTextJson = getDummyObject();


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //setContentView(R.layout.fragment_get_api);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new APIGetFragment()).commit();

        SimpleFragment simpleFragment = new SimpleFragment();
        fragmentTransaction.add(R.id.item1, simpleFragment);
        //fragmentTransaction.commit();
        DescriptionFragment descriptionFragment1= new DescriptionFragment();
        fragmentTransaction.add(R.id.item2, descriptionFragment1);
        SimpleFragment simpleFragment1= new SimpleFragment();
        fragmentTransaction.add(R.id.item3, simpleFragment1);
        DescriptionFragment descriptionFragment3= new DescriptionFragment();
        fragmentTransaction.add(R.id.item4, descriptionFragment3);

        fragmentTransaction.commit();



       // int spacesToIndentEachLevel = 2;
        //JSONObject json = new JSONObject(loadJSONFromAsset(getApplicationContext())).toString(spacesToIndentEachLevel);
        //String json = new String(loadJSONFromAsset(getApplicationContext()));
        System.out.println(dummyTextJson.toString(4));
    }
    public JSONObject getDummyObject() throws JSONException {
        JSONObject json = new JSONObject();



        JSONArray jsonArray = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("name" , "pulp fiction");
        obj.put("type" , "movie");
        obj.put("wTeaser" , "\n\nPulp Fiction is a 1994 American crime thriller drama film written and directed by Quentin Tarantino; it is based on a story by Tarantino and Roger Avary. Starring John Travolta, Samuel ");
        obj.put("wUrl" , "http://en.wikipedia.org/wiki/Pulp_Fiction_(movie)");
        obj.put("yUrl" , "https://www.youtube-nocookie.com/embed/s7EdQ4FqbhY");
        obj.put("yID" , "s7EdQ4FqbhY");
        jsonArray.put(obj);

        json.put("similar", jsonArray);


        return json;
    }


}
