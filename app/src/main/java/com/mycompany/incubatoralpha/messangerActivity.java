package com.mycompany.incubatoralpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class messangerActivity extends AppCompatActivity {

    private ListView myListView, myPendView;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messanger);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myListView = (ListView) findViewById(R.id.messangerListView);
        myPendView = (ListView) findViewById(R.id.pendingListView);

        userLocalStore = new UserLocalStore(this);





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
            startActivity(new Intent(messangerActivity.this, LoginActivity.class));
        }
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();
    }

    private void showIdeas(){
        JSONObject jsonObject;
        User user = userLocalStore.getLoggedInUser();

        ArrayList<String> test = new ArrayList<>();
        ArrayList<String> test2 = new ArrayList<>();

        try {
            jsonObject = new JSONObject(userLocalStore.getUserConnectData());
            JSONArray result = jsonObject.getJSONArray(UserLocalStore.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String fname = jo.getString(UserLocalStore.TAG_connectFname);
                String lname = jo.getString(UserLocalStore.TAG_connectLname);
                String otherUID = jo.getString(UserLocalStore.TAG_connectUID);

                test.add(fname + " " + lname);
                test2.add(otherUID);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> myValues = new ArrayList<>();

        try {
            jsonObject = new JSONObject(userLocalStore.getContacts());
            JSONArray result = jsonObject.getJSONArray(UserLocalStore.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String fname = jo.getString(UserLocalStore.TAG_contactFname);
                String lname = jo.getString(UserLocalStore.TAG_contactLname);
                String otherUID = jo.getString(UserLocalStore.TAG_contactUID);

                myValues.add(fname + " " + lname);
                //myValues.add(otherUID);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomAdapterMes dud = new CustomAdapterMes(this,test,user.uid, test2);
        myPendView.setAdapter(dud);
        setListViewHeightBasedOnChildren(myPendView);

        ArrayAdapter<String> myAdaptor = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,myValues);

        myListView.setAdapter(myAdaptor);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String myItem = (String) myListView.getItemAtPosition(position);
                //Toast.makeText(getBaseContext(),myItem,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(messangerActivity.this, peopleMessangerActivity.class));
            }
        });
    }

    private void getJSON(final User user) {
        final ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserConnectInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
            }

            @Override
            public void donezo(String s) {
                userLocalStore.storeUserConnect(s);
                serverRequests.fetchContactsInBackground(user, new GetUserCallback() {
                            @Override
                            public void done(User returnedUser) {

                            }

                            @Override
                            public void donezo(String string) {
                                userLocalStore.storeUserContacts(string);
                                displayUserDetails();
                                showIdeas();
                            }
                        }
                );
            }
        });}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.action_discover:
                startActivity(new Intent(messangerActivity.this, DiscoverActivity.class));
                return true;

            case R.id.action_profile:
                startActivity(new Intent(messangerActivity.this, MainActivity.class));
                return true;

            case R.id.action_idea:
                startActivity(new Intent(messangerActivity.this, ideaActivity.class));
                return true;

            case R.id.action_messenger:
                startActivity(new Intent(messangerActivity.this, messangerActivity.class));
                return true;

            case R.id.menuLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(messangerActivity.this, StartActivity.class));

                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ArrayAdapter listAdapter = (ArrayAdapter)listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}

