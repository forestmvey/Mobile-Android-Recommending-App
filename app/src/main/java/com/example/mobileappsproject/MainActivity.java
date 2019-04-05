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

    // set verbose level
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

        // instantiate all Arraylists  to contain saved frgments
        musicArray = new ArrayList<JSONObject>();
        moviesArray = new ArrayList<JSONObject>();
        showsArray = new ArrayList<JSONObject>();
        authorsArray = new ArrayList<JSONObject>();
        booksArray = new ArrayList<JSONObject>();
        gamesArray = new ArrayList<JSONObject>();
        podcastsArray = new ArrayList<JSONObject>();

        // update all of the types in object to set the url GET reqeust parameters
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
    // Select navigation page
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        try {
        switch(item.getItemId()) {
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
                break;
            case R.id.nav_movies:
                moviesArray = readJSONFromMemory("movie", MOVIES_FILE, moviesArray);
                if(V)System.out.println("movies array before navigation = " + moviesArray.toString());
                addSavedFragment(moviesArray);
                break;
            case R.id.nav_music:
                musicArray = readJSONFromMemory("music", MUSIC_FILE, musicArray);
                if(V)if(V)System.out.println("music array before navigation = " + musicArray.toString());
                addSavedFragment(musicArray);
                break;
            case R.id.nav_shows:
                showsArray = readJSONFromMemory("show", SHOWS_FILE, showsArray);
                addSavedFragment(showsArray);
                break;
            case R.id.nav_books:
                booksArray = readJSONFromMemory("book", BOOKS_FILE, booksArray);
                if(V)System.out.println("music array before navigation = " + booksArray.toString());
                addSavedFragment(booksArray);
                break;
            case R.id.nav_authors:
                authorsArray = readJSONFromMemory("author", AUTHORS_FILE, authorsArray);
                if(V)System.out.println("music array before navigation = " + authorsArray.toString());
                addSavedFragment(authorsArray);
                break;
            case R.id.nav_games:
                gamesArray = readJSONFromMemory("game", GAMES_FILE, gamesArray);
                if(V)System.out.println("games array before navigation = " + gamesArray.toString());
                addSavedFragment(gamesArray);
                break;
            case R.id.nav_podcasts:
                podcastsArray = readJSONFromMemory("podcast", PODCASTS_FILE, podcastsArray);
                if(V)System.out.println("podcasts array before navigation = " + podcastsArray.toString());
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

    // prepare JSON array of saved objects to be added to fragments
    public void addSavedFragment(ArrayList<JSONObject> myList) throws JSONException {

        jArr = new JSONArray();
        for(int i = 0; i < myList.size(); i++) {
            JSONObject x = myList.get(i);
            System.out.println("the string is = " + x.toString());
            jArr.put(x);
        }
        addFragments(jArr, false);

    }

    // prepare JSON string to be added to fragments
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
        addFragments(jArr, true);
        }


        // Adds json items to fragments and adds fragment to the fragment manager
        // to display fragments of type on navigation
        private void addFragments(JSONArray jArr, boolean addOrRemove) throws JSONException {
            fragsToInstantiate = 0;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new APIGetFragment()).commit();

            ArrayList<DescriptionFragment> FRAGMENTS = new ArrayList<>();


            // iterate through all retrieved data to add object values into fragment
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
                    if(keys.hasNext()){
                        key = keys.next();
                        sYoutube = json.getString(key);}
                    FRAGMENTS.add(DescriptionFragment.newInstance(sName, sType, sDescription, sWiki, sYoutube, addOrRemove));
                    fragsToInstantiate += 1;
                    break;
                }
        }


            // Iterate through retrieved fragments and add fragments into fragment manager
            // API returns a maximum 20 items
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


    // read saved JSON from sharedPreferences, and add to array list
    private ArrayList<JSONObject> readJSONFromMemory(String myList, String saveFile, ArrayList<JSONObject> arr) {
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
        return arr;
    }


    // retrieve JSON from URL GET request
    public void getJSON() throws JSONException {
        url = setUrl();
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

    // Change url parameters when checkbox typelist is checked
    public void setTypeList(String type, boolean checkOrNo) {
        if(checkOrNo){
            typesCheckList.add(type);
            if(VV)System.out.println("adding");
        } else{
            typesCheckList.remove(type);
            if(VV)System.out.println("deleting");
        }
    }

    // set URL for GET request
    public String setUrl() throws JSONException {
        url = baseUrl + searchName;
        for(int i = 0; i < typesCheckList.size(); i++) {
            if(typesObj.has(typesCheckList.get(i))){
                url += typesObj.getString(typesCheckList.get(i));
            }
        }
        if(V)System.out.println(url);
        return url;
    }

    // Set the search name for the URL GET request
    public void setSearchName(String name) {

        searchName = "&q=" + name;
        System.out.println("searchName changing = " + searchName);
    }

    // Iterate through Array to either save object in array to memory
    // or delete object from memory
    public void saveOrDeleteJson(String name, String type, boolean saveOrDelete) throws JSONException {
        System.out.println("name = " + name + " type = " + type);
        JSONObject jObj;
        for(int i = 0; i < jArr.length(); i++) {
            jObj = jArr.getJSONObject(i);
            if(jObj.getString("Name").equals(name) && jObj.getString("Type").equals(type)){
                System.out.println("saving object");
                saveJsonObject(jArr.getJSONObject(i), type, saveOrDelete);
                }
        }
    }

    // Save or Delete object from local memory
    public void saveJsonObject(JSONObject jObj, String type, boolean saveOrDelete) throws JSONException {
        String toast;
        if(saveOrDelete) {
            toast = "Item Saved";
        }else {
            toast = "Item Removed!";
        }
        switch(type) {
            case "book":
                if(saveOrDelete) {
                    booksArray.add(jObj);
                }else {
                    booksArray.remove(jObj);
                    addSavedFragment(booksArray);
                }
                if(V)System.out.println("booksArray = " + booksArray.toString());
                saveJSON(type, BOOKS_FILE, booksArray);
                break;
            case "music":
                if(saveOrDelete) {
                    musicArray.add(jObj);
                } else {
                    musicArray.remove(jObj);
                    addSavedFragment(musicArray);
                }
                    if(V)System.out.println("type = " + type + " musicArray = " + musicArray.toString());
                    saveJSON(type, MUSIC_FILE, musicArray);
                    break;
            case "author":
               if(saveOrDelete) {
                   authorsArray.add(jObj);
               }else{
                   authorsArray.remove(jObj);
                   addSavedFragment(authorsArray);
               }
                    if(V)System.out.println("authorsArray = " + authorsArray.toString());
                saveJSON(type, AUTHORS_FILE, authorsArray);
                break;
            case "movie":
                if(saveOrDelete) {
                    moviesArray.add(jObj);
                }else {
                    moviesArray.remove(jObj);
                    addSavedFragment(moviesArray);
                    }
                    System.out.println("moviesArray = " + moviesArray.toString());
                saveJSON(type, MOVIES_FILE, moviesArray);
                saveJSON(type, MUSIC_FILE, musicArray);
                break;
            case "show":
                if(saveOrDelete) {
                    showsArray.add(jObj);
                }else{
                    showsArray.remove(jObj);
                    addSavedFragment(showsArray);
                }
                    if(V)System.out.println("showsArray = " + showsArray.toString());
                saveJSON(type, SHOWS_FILE, showsArray);
                break;
            case "podcast":
                if(saveOrDelete) {
                    podcastsArray.add(jObj);
                }else {
                    podcastsArray.remove(jObj);
                    addSavedFragment(podcastsArray);
                }
                    if(V)System.out.println("podcastsArray = " + podcastsArray.toString());
                saveJSON(type, PODCASTS_FILE, podcastsArray);
                break;
            case "game":
                if(saveOrDelete) {
                    gamesArray.add(jObj);
                }else{
                    gamesArray.remove(jObj);
                    addSavedFragment(gamesArray);
                }
                    if(V)System.out.println("gamesArray = " + gamesArray.toString());
                saveJSON(type, GAMES_FILE, gamesArray);
                break;
        }
        Toast.makeText(this, toast,
                Toast.LENGTH_SHORT).show();
    }

    // Save JSON object to sharedPreferences
    private void saveJSON(String myList, String saveFile, ArrayList<JSONObject> arr) {
        SharedPreferences sharedPreferences = getSharedPreferences(saveFile, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String typeList = gson.toJson(arr);
        prefEditor.putString( myList, typeList );
        System.out.println("saveJDON myList = " + myList);
        prefEditor.apply();
    }
}
