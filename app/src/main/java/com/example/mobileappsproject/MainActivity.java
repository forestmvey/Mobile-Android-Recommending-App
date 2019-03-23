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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    private String sName;
    private String sType;
    private String sDescription;
    private String sWiki;
    private String sYoutube;

    String myResponse;

    private String baseUrl = "http://tastedive.com/api/similar?k=329970-moverecc-O6MBUCNY&q=pulp+fiction";

    private JSONObject typesObj;
    public ArrayList<String> typesCheckList;

    private int fragsToInstantiate;


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

        typesObj = new JSONObject();
        typesCheckList = new ArrayList<>();
        try {
            typesObj.put("typeBook" , "&type=book");
            typesObj.put("typeMovie" , "&type=movie");
            typesObj.put("typeMusic" , "&type=music");
            typesObj.put("typePodcast" , "&type=podcast");
            typesObj.put("typeAuthor" , "&type=author");
            typesObj.put("typeGame" , "&type=game");
            typesObj.put("typeShow" , "&type=show");
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
        //JSONObject ary = new JSONObject(getJSON(setAsyncUrl()));
        //System.out.println("working?" + ary.toString());
        JSONObject dummyTextJson = getDummyObject();
        //System.out.println(dummyTextJson.toString());
        JSONArray arr = dummyTextJson.getJSONArray("similar");
        //System.out.println(arr.toString());
//        System.out.println(arr.get(0).toString().indexOf(0));

        fragsToInstantiate = 0;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new APIGetFragment()).commit();


        ArrayList<DescriptionFragment> FRAGMENTS = new ArrayList<>();


        for (int i = 0; i < arr.length(); i++) {
           // System.out.println("arr len = " + arr.length());
            JSONObject json = arr.getJSONObject(i);
            //System.out.println(json.toString());
            Iterator<String> keys = json.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                //System.out.println("Key :" + key + "  Value :" + json.get(key));
                //System.out.println(json.getString(key));
                sName = json.getString(key);
                key = keys.next();
                sType = json.getString(key);
                key = keys.next();
                sDescription = json.getString(key);
                key = keys.next();
                sWiki = json.getString(key);
                key = keys.next();
                sYoutube = json.getString(key);
                FRAGMENTS.add(DescriptionFragment.newInstance(sName, sType, sDescription, sWiki, sYoutube));
                fragsToInstantiate += 1;
                break;
            }
        }

        for(int i = 0; i < fragsToInstantiate; i++) {
            switch(i){
                case 0:
                    fragmentTransaction.add(R.id.item0, FRAGMENTS.get(i));
                    break;
                case 1:
                    fragmentTransaction.add(R.id.item1, FRAGMENTS.get(i));
                    break;
                case 2:
                    fragmentTransaction.add(R.id.item2, FRAGMENTS.get(i));
                    break;
                case 3:
                    fragmentTransaction.add(R.id.item3, FRAGMENTS.get(i));
                    break;
                case 4:
                    fragmentTransaction.add(R.id.item4, FRAGMENTS.get(i));
                    break;
                case 5:
                    fragmentTransaction.add(R.id.item5, FRAGMENTS.get(i));
                    break;
                case 6:
                    fragmentTransaction.add(R.id.item6, FRAGMENTS.get(i));
                    break;
                case 7:
                    fragmentTransaction.add(R.id.item7, FRAGMENTS.get(i));
                    break;
                case 8:
                    fragmentTransaction.add(R.id.item8, FRAGMENTS.get(i));
                    break;
                case 9:
                    fragmentTransaction.add(R.id.item9, FRAGMENTS.get(i));
                    break;
                case 10:
                    fragmentTransaction.add(R.id.item10, FRAGMENTS.get(i));
                    break;
                case 11:
                    fragmentTransaction.add(R.id.item11, FRAGMENTS.get(i));
                    break;
                case 12:
                    fragmentTransaction.add(R.id.item12, FRAGMENTS.get(i));
                    break;
                case 13:
                    fragmentTransaction.add(R.id.item13, FRAGMENTS.get(i));
                    break;
                case 14:
                    fragmentTransaction.add(R.id.item14, FRAGMENTS.get(i));
                    break;
                case 15:
                    fragmentTransaction.add(R.id.item15, FRAGMENTS.get(i));
                    break;
                case 16:
                    fragmentTransaction.add(R.id.item16, FRAGMENTS.get(i));
                    break;
                case 17:
                    fragmentTransaction.add(R.id.item17, FRAGMENTS.get(i));
                    break;
                case 18:
                    fragmentTransaction.add(R.id.item18, FRAGMENTS.get(i));
                    break;
                case 19:
                    fragmentTransaction.add(R.id.item19, FRAGMENTS.get(i));
                    break;
            }
        }
        fragmentTransaction.commit();
        System.out.println(getJSON(setAsyncUrl()));
    }

    public String getJSON(String url) {

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    myResponse = response.body().string();
                    System.out.println(myResponse);

//                    MainActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                setAsyncUrl();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
                }
            }
        });
        return myResponse;
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
        obj = new JSONObject();
        obj.put("name" , "harry potter");
        obj.put("type" , "movie");
        obj.put("wTeaser" , "\n\n I don't really want to write about harry potter. It's a really bad movie but I haven't seen it in a long time so meh");
        obj.put("wUrl" , "http://en.wikipedia.org/wiki/harry_potter_(movie)");
        obj.put("yUrl" , "https://www.youtube-nocookie.com/embed/s7EdQ4FqbhY");
        obj.put("yID" , "s7EdQ4FqbhY");
        jsonArray.put(obj);

        json.put("similar", jsonArray);

        obj = new JSONObject();
        obj.put("name" , "sword fish");
        obj.put("type" , "movie");
        obj.put("wTeaser" , "\n\n don't remember a whole lot of this movie! JOHN TRAVOLTA");
        obj.put("wUrl" , "http://en.wikipedia.org/wiki/sword_fish_(movie)");
        obj.put("yUrl" , "https://www.youtube-nocookie.com/embed/s7EdQ4FqbhY");
        obj.put("yID" , "s7EdQ4FqbhY");
        jsonArray.put(obj);

        json.put("similar", jsonArray);


        return json;
    }

    public void setTypeList(String type, boolean checkOrNo) {
        if(checkOrNo){
            typesCheckList.add(type);
            //System.out.println("adding");
        } else{
            typesCheckList.remove(type);
            //System.out.println("deleting");
        }
    }

    public String setAsyncUrl() throws JSONException {
        String url = baseUrl;
        for(int i = 0; i < typesCheckList.size(); i++) {
           if(typesObj.has(typesCheckList.get(i))){
               url += typesObj.getString(typesCheckList.get(i));
           }
        }
        return url;
    }


}
