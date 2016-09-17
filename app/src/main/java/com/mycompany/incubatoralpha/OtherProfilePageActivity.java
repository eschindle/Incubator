package com.mycompany.incubatoralpha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class OtherProfilePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile_page);
    }

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

        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.action_discover:
                startActivity(new Intent(OtherProfilePageActivity.this, DiscoverActivity.class));
                return true;

            case R.id.action_profile:
                startActivity(new Intent(OtherProfilePageActivity.this, MainActivity.class));
                return true;

            case R.id.action_idea:
                startActivity(new Intent(OtherProfilePageActivity.this, ideaActivity.class));
                return true;

            case R.id.action_messenger:
                startActivity(new Intent(OtherProfilePageActivity.this, messangerActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
