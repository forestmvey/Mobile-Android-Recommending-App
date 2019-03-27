package com.example.mobileappsproject;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
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

    public String searchName;
    private String baseUrl = "http://tastedive.com/api/similar?info=1&k=329970-moverecc-O6MBUCNY";
    private String url;

    private JSONObject typesObj;
    public ArrayList<String> typesCheckList;

    private JSONObject jObj;
    private JSONArray jArr;
    public String jString;

    private final String MUSIC_FILE= "musicFile";
    private final String MOVIES_FILE = "moviesFile";
    private final String AUTHORS_FILE = "authorsFile";
    private final String SHOWS_FILE = "showsFile";
    private final String BOOKS_FILE = "booksFile";
    private final String PODCASTS_FILE = "podcastsFile";
    private final String GAMES_FILE = "gamesFile";
    private ArrayList<JSONObject> moviesArray;
    private ArrayList<JSONObject> authorsArray;
    private ArrayList<JSONObject> showsArray;
    private ArrayList<JSONObject> musicArray;
    private ArrayList<JSONObject> booksArray;
    private ArrayList<JSONObject> podcastsArray;
    private ArrayList<JSONObject> gamesArray;

    private boolean VV = false;
    private boolean V = false;

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

        musicArray = new ArrayList<JSONObject>();
        moviesArray = new ArrayList<JSONObject>();
        showsArray = new ArrayList<JSONObject>();
        authorsArray = new ArrayList<JSONObject>();
        booksArray = new ArrayList<JSONObject>();
        gamesArray = new ArrayList<JSONObject>();
        podcastsArray = new ArrayList<JSONObject>();
        //musicArray = readJSON("music", MUSIC_FILE, musicArray);
        //System.out.println("booksArray after readJSON = " + musicArray.toString());


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
        try {
        switch(item.getItemId()) {
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
                break;
            case R.id.nav_movies:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new MovieFragment()).commit();
                moviesArray = readJSON("movie", MOVIES_FILE, moviesArray);
                System.out.println("movies array before navigation = " + moviesArray.toString());
                addSavedFragment(moviesArray);
                break;
            case R.id.nav_music:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new MusicFragment()).commit();
                musicArray = readJSON("music", MUSIC_FILE, musicArray);
                if(V)System.out.println("music array before navigation = " + musicArray.toString());
                addSavedFragment(musicArray);
                break;
            case R.id.nav_shows:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new MusicFragment()).commit();
                showsArray = readJSON("show", SHOWS_FILE, showsArray);
                addSavedFragment(showsArray);
                break;
            case R.id.nav_books:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new MusicFragment()).commit();
                booksArray = readJSON("books", BOOKS_FILE, booksArray);
                addSavedFragment(booksArray);
                break;
            case R.id.nav_authors:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new MusicFragment()).commit();
                authorsArray = readJSON("author", AUTHORS_FILE, authorsArray);
                addSavedFragment(authorsArray);
                break;
            case R.id.nav_games:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new MusicFragment()).commit();
                gamesArray = readJSON("game", GAMES_FILE, gamesArray);
                addSavedFragment(gamesArray);
                break;
            case R.id.nav_podcasts:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new MusicFragment()).commit();
                podcastsArray = readJSON("podcast", PODCASTS_FILE, podcastsArray);
                addSavedFragment(podcastsArray);
                break;
        }
        } catch (JSONException e) {
            e.printStackTrace();
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

    public void addSavedFragment(ArrayList<JSONObject> myList) throws JSONException {

        jArr = new JSONArray();
        for(int i = 0; i < myList.size(); i++) {
            JSONObject x = myList.get(i);
            System.out.println("the string is = " + x.toString());
            jArr.put(x);
        }
        addFragments(jArr);
    }

    public void addSearchFragment(String jString) throws JSONException {

        jObj = new JSONObject(jString)
                .getJSONObject("Similar");
        jArr = new JSONArray();
        JSONArray newJArray = jObj.getJSONArray("Results");
        if(V)System.out.println("jString = " + jString);

        for (int i = 0; i < newJArray.length(); i++) {
            jObj = newJArray.getJSONObject(i);
            jArr.put(jObj);
        }

        if(V)System.out.println("jArr = " + jArr.toString());
        addFragments(jArr);
        }

        private void addFragments(JSONArray jArr) throws JSONException {
            fragsToInstantiate = 0;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new APIGetFragment()).commit();

            ArrayList<DescriptionFragment> FRAGMENTS = new ArrayList<>();

            for (int i = 0; i < jArr.length(); i++) {
                JSONObject json = jArr.getJSONObject(i);
                Iterator<String> keys = json.keys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    if(VV)System.out.println("Key :" + key + "  Value :" + json.get(key));
                    if(VV)System.out.println(json.getString(key));
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
    }

    private void saveJSON(String myList, String saveFile, ArrayList<JSONObject> arr) {
        SharedPreferences sharedPreferences = getSharedPreferences(saveFile, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        System.out.println("arr = " + arr.toString());
        String typeList = gson.toJson(arr);
        prefEditor.putString( myList, typeList );
        prefEditor.apply();
        System.out.println("Saving JSON myList = " + myList);
        System.out.println("saving this item " + sharedPreferences.getString(myList, null));
    }

    private ArrayList<JSONObject> readJSON(String myList, String saveFile, ArrayList<JSONObject> arr) {
        SharedPreferences prefs = getSharedPreferences(saveFile, Context.MODE_PRIVATE);
        String savedJSON = prefs.getString(myList, null);
        System.out.println("reading these items " + savedJSON + "myList = " + myList);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<JSONObject>>() {}.getType();
        arr = gson.fromJson(savedJSON, type);


        if(arr == null) {
            arr = new ArrayList<JSONObject>();
            System.out.println("readJSON arr = " + arr.toString());
        }
        System.out.println("after if arr = " + arr.toString() + " moviesArr = " + moviesArray.toString());
        return arr;
    }



    public void getJSON() throws JSONException {
        url = setAsyncUrl();
        System.out.println("after setAsyncUrl url = " + url);
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
                    jString = new String(response.body().string());
                    if(V)System.out.println("jString = " + jString);
                    try {
                        addSearchFragment(jString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setTypeList(String type, boolean checkOrNo) {
        if(checkOrNo){
            typesCheckList.add(type);
            if(VV)System.out.println("adding");
        } else{
            typesCheckList.remove(type);
            if(VV)System.out.println("deleting");
        }
    }

    public String setAsyncUrl() throws JSONException {
        url = baseUrl + searchName;
        for(int i = 0; i < typesCheckList.size(); i++) {
            if(typesObj.has(typesCheckList.get(i))){
                url += typesObj.getString(typesCheckList.get(i));
            }
        }
        if(V)System.out.println(url);
        return url;
    }
    public void setSearchName(String name) {

        searchName = "&q=" + name;
        //System.out.println("searchName changing = " + searchName);
    }

    public void saveToJson(String name, String type) throws JSONException {
        System.out.println("name = " + name + " type = " + type);
        JSONObject jObj;
        for(int i = 0; i < jArr.length(); i++) {
            jObj = jArr.getJSONObject(i);
            if(jObj.getString("Name").equals(name) && jObj.getString("Type").equals(type)){
                System.out.println("saving object");
                saveJsonObject(jArr.getJSONObject(i), type);
            }
        }
    }
    public void saveJsonObject(JSONObject jObj, String type) {

        switch(type) {
            case "book":
                booksArray.add(jObj);
                if(V)System.out.println("booksArray = " + booksArray.toString());
                saveJSON(type, "booksFile", booksArray);
                break;
            case "music":
                musicArray.add(jObj);
                if(V)System.out.println("type = " + type + " musicArray = " + musicArray.toString());
                saveJSON(type, MUSIC_FILE, musicArray);
                break;
            case "author":
               authorsArray.add(jObj);
                if(V)System.out.println("authorsArray = " + authorsArray.toString());
                saveJSON(type, AUTHORS_FILE, authorsArray);
                break;
            case "movie":
                moviesArray.add(jObj);
                System.out.println("moviesArray = " + moviesArray.toString());
                saveJSON(type, MOVIES_FILE, moviesArray);
                break;
            case "show":
                showsArray.add(jObj);
                if(V)System.out.println("showsArray = " + showsArray.toString());
                saveJSON(type, SHOWS_FILE, showsArray);
                break;
            case "podcast":
                podcastsArray.add(jObj);
                if(V)System.out.println("podcastsArray = " + podcastsArray.toString());
                saveJSON(type, PODCASTS_FILE, podcastsArray);
                break;
            case "game":
                gamesArray.add(jObj);
                if(V)System.out.println("gamesArray = " + gamesArray.toString());
                saveJSON(type, GAMES_FILE, gamesArray);
                break;
        }
    }
}
