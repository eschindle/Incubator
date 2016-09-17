package com.mycompany.incubatoralpha;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DiscoverActivity extends AppCompatActivity {

    //private ListView discoverListView;
    UserLocalStore userLocalStore;


    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    private final static String TAG = "TestActivity";

    private ArrayList<String> a1;
    private ArrayList<String> a2;
    private ArrayAdapter<String> arrayAdapter;
    //private int i;

    @InjectView(R.id.frame) SwipeFlingAdapterView flingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        //discoverListView = (ListView) findViewById(R.id.discoverListView);
        Log.i(TAG, "On Create .....");


        userLocalStore = new UserLocalStore(this);


        loadActivity();



    }

    private void loadActivity() {
        // Do all of your work here
        final User user = userLocalStore.getLoggedInUser();
        getJSON(user);


        ButterKnife.inject(this);

        a1 = new ArrayList<>();
        a2 = new ArrayList<>();
        ArrayList<ArrayList<String>> result;
        result = showIdeas();
        a1 = result.get(1);
        a2 = result.get(0);

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, a2 );


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {

            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                a2.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject

                String test = (String) dataObject;

                String name = "";
                String desc = "";
                String tname = "";
                String tdesc = "";
                String iid = "";
                String ouid = "";

                if (a1.size() > 0){
                    name = test.split("\n\n")[0];
                    desc = test.split("\n\n")[1];
                    for (int i = 0; i < a1.size(); i++){
                        tname = a1.get(i).split("\n\n")[0];
                        tdesc = a1.get(i).split("\n\n")[1];

                        if (Objects.equals(name + desc, tname + tdesc)) {
                            iid = a1.get(i).split("\n\n")[2];
                            ouid = a1.get(i).split("\n\n")[3];
                        }
                    }
                }

                int newIid = Integer.parseInt(iid);
                int newOuid = Integer.parseInt(ouid);
                System.out.println(newOuid);

                Idea idea = new Idea(user.uid,newIid,0,newOuid);
                SwipeServerRequests serverRequests = new SwipeServerRequests(this);
                serverRequests.storeLikeInBackground(idea, new GetIdeaCallback() {
                    @Override
                    public void done(Idea returnedIdea) {
                        makeToast(DiscoverActivity.this, "Pass!");
                    }
                });
            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onRightCardExit(Object dataObject) {

                String test = (String) dataObject;

                String name = "";
                String desc = "";
                String tname = "";
                String tdesc = "";
                String iid = "";
                String ouid = "";

                if (a1.size() > 0){
                    name = test.split("\n\n")[0];
                    desc = test.split("\n\n")[1];
                    for (int i = 0; i < a1.size(); i++){
                        tname = a1.get(i).split("\n\n")[0];
                        tdesc = a1.get(i).split("\n\n")[1];

                        if (Objects.equals(name + desc, tname + tdesc)) {
                            iid = a1.get(i).split("\n\n")[2];
                            ouid = a1.get(i).split("\n\n")[3];
                        }
                    }
                }

                int newIid = Integer.parseInt(iid);
                int newOuid = Integer.parseInt(ouid);
                System.out.println(newOuid);

                Idea idea = new Idea(user.uid,newIid,1,newOuid);
                SwipeServerRequests serverRequests = new SwipeServerRequests(this);
                serverRequests.storeLikeInBackground(idea, new GetIdeaCallback() {
                    @Override
                    public void done(Idea returnedIdea) {
                        makeToast(DiscoverActivity.this, "Liked!");
                    }
                });


            }


            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                /*
                a2.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;*/
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(DiscoverActivity.this, "Hello!");
            }
        });
    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.right)
    public void right() {
        /**
         * Trigger the right event manually.
         */
        flingContainer.getTopCardListener().selectRight();
    }

    @OnClick(R.id.left)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }

        //This is what controls where you click to move to another page...Don't know if it will work on unique data
        /*
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];

        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_profile, "Profile");
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_messanger, "Messenger");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_idea, "Idea");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_discover,"Discover");
        drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_logout,"Logout");

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close)
        {


            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {
        switch (position) {
            case 0:
                Intent myProfInt = new Intent(this, MainActivity.class);
                myProfInt.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myProfInt);
                break;
            case 1:
                Intent myMesInt = new Intent(this, messangerActivity.class);
                myMesInt.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myMesInt);
                break;
            case 2:
                Intent myIdeaInt = new Intent(this, ideaActivity.class);
                myIdeaInt.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIdeaInt);
                break;

            case 3:
                Intent myDiscoverInt = new Intent(this, DiscoverActivity.class);
                myDiscoverInt.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myDiscoverInt);
                break;
            case 4:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                Intent logoutIntent = new Intent(this,StartActivity.class);
                startActivity(logoutIntent);
                finish();
                break;

            default:
                break;
        }
    }*/

    private void getJSON(User user) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchOtherUserIdeasInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
            }

            @Override
            public void donezo(String s) {
                userLocalStore.storeOtherIdeaData(s);
                displayUserDetails();
                showIdeas();
            }
        });
    }

    private ArrayList<ArrayList<String>> showIdeas(){
        JSONObject jsonObject;

        ArrayList<String> test = new ArrayList<>();
        ArrayList<String> test2 = new ArrayList<>();

        try {
            jsonObject = new JSONObject(userLocalStore.getOtherIdeaData());
            JSONArray result = jsonObject.getJSONArray(UserLocalStore.TAG_JSON_ARRAY);
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(UserLocalStore.TAG_otherIdeaID);
                String name = jo.getString(UserLocalStore.TAG_otherIdeaName);
                String desc = jo.getString(UserLocalStore.TAG_otherDesc);
                String uid = jo.getString(UserLocalStore.TAG_otherUID);

                test.add(name + "\n\n" + desc);
                test2.add(name + "\n\n" + desc + "\n\n" + id + "\n\n" + uid);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            recreate();
        }

        ArrayList<ArrayList<String>> result = new ArrayList<>();
        result.add(test);
        result.add(test2);

        return result;

        /*
        ListAdapter adapter = new SimpleAdapter(this, list, R.layout.list_item, theVals, theIds);

        discoverListView.setAdapter(adapter);
        discoverListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent disInt = new Intent(DiscoverActivity.this, IdeaSwipeActivity.class);
                disInt.putExtra("Spot",position);
                startActivity(disInt);
            }
        });
        */
    }


    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate()){
            User user = userLocalStore.getLoggedInUser();

        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        ((AppCompatActivity)DiscoverActivity.this).getSupportActionBar().setTitle(mTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        User user = userLocalStore.getLoggedInUser();
        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.action_discover:
                getJSON(user);
                startActivity(new Intent(this, DiscoverActivity.class));
                return true;

            case R.id.action_profile:
                getJSON(user);
                startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.action_idea:
                getJSON(user);
                startActivity(new Intent(this, ideaActivity.class));
                return true;

            case R.id.action_messenger:
                getJSON(user);
                startActivity(new Intent(this, messangerActivity.class));
                return true;

            case R.id.menuLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, StartActivity.class));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
