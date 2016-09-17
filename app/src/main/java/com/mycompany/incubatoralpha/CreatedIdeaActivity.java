package com.mycompany.incubatoralpha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class CreatedIdeaActivity extends AppCompatActivity {

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_idea);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        userLocalStore = new UserLocalStore(this);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate()){
            displayUserDetails();
        }else{
            startActivity(new Intent(CreatedIdeaActivity.this, LoginActivity.class));
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
                startActivity(new Intent(CreatedIdeaActivity.this, DiscoverActivity.class));
                return true;

            case R.id.action_profile:
                startActivity(new Intent(CreatedIdeaActivity.this, MainActivity.class));
                return true;

            case R.id.action_idea:
                startActivity(new Intent(CreatedIdeaActivity.this, ideaActivity.class));
                return true;

            case R.id.action_messenger:
                startActivity(new Intent(CreatedIdeaActivity.this, messangerActivity.class));
                return true;

            case R.id.menuLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(CreatedIdeaActivity.this, StartActivity.class));

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
