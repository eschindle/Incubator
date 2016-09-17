package com.mycompany.incubatoralpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SkillActivity extends AppCompatActivity implements View.OnClickListener {

    UserLocalStore userLocalStore;
    private EditText editTextSkillName;
    Button addSkill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userLocalStore = new UserLocalStore(this);

        editTextSkillName = (EditText) findViewById(R.id.editTextSkill);

        addSkill = (Button) findViewById(R.id.buttonAddSkill);
        addSkill.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAddSkill:
                String skill  = editTextSkillName.getText().toString();

                User user = userLocalStore.getLoggedInUser();
                int uid = user.uid;

                User eUser = new User(uid, skill);

                if (!validate()) {
                    return;
                }

                addSki(eUser);
                break;
        }
    }

    public boolean validate() {
        boolean valid = true;

        String skill = editTextSkillName.getText().toString();

        if (skill.isEmpty() || skill.length() < 3 || skill.length() > 30) {
            editTextSkillName.setError("between 3 and 30 characters");
            valid = false;
        } else {
            editTextSkillName.setError(null);
        }


        return valid;
    }

    private void addSki(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserSkillInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(SkillActivity.this, MainActivity.class));
            }

            @Override
            public void donezo(String string) {

            }
        });
    }
}
