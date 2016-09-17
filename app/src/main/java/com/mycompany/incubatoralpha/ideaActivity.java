package com.mycompany.incubatoralpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ideaActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView ideaListView;
    UserLocalStore userLocalStore;
    FloatingActionButton createIdea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ideaListView = (ListView) findViewById(R.id.idListView);

        userLocalStore = new UserLocalStore(this);

        createIdea = (FloatingActionButton) findViewById(R.id.buttonCreateIdea);

        createIdea.setOnClickListener(this);
    }

    private void getJSON(User user) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserIdeasInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
            }

            @Override
            public void donezo(String s) {
                userLocalStore.storeIdeaData(s);
                displayUserDetails();
                showIdeas();
            }
        });
    }

    private void showIdeas(){
        JSONObject jsonObject;
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        try {
            jsonObject = new JSONObject(userLocalStore.getIdeaData());
            JSONArray result = jsonObject.getJSONArray(UserLocalStore.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String name = jo.getString(UserLocalStore.TAG_ideaName);
                String desc = jo.getString(UserLocalStore.TAG_desc);

                HashMap<String,String> ideas = new HashMap<>();
                ideas.put(UserLocalStore.TAG_ideaName,name);
                ideas.put(UserLocalStore.TAG_desc,desc);
                list.add(ideas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ideaActivity.this, list, R.layout.list_item,
                new String[]{UserLocalStore.TAG_ideaName,UserLocalStore.TAG_desc},
                new int[]{R.id.name, R.id.desc});

        ideaListView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate()){
            User user = userLocalStore.getLoggedInUser();

            getJSON(user);
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.action_discover:
                startActivity(new Intent(this, DiscoverActivity.class));
                return true;

            case R.id.action_profile:
                startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.action_idea:
                startActivity(new Intent(this, ideaActivity.class));
                return true;

            case R.id.action_messenger:
                startActivity(new Intent(this, messangerActivity.class));
                return true;

            case R.id.menuLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, StartActivity.class));

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonCreateIdea:
                startActivity(new Intent(ideaActivity.this, IncubateActivity.class));

                break;
        }
    }


}
