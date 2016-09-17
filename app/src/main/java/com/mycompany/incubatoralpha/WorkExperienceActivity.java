package com.mycompany.incubatoralpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WorkExperienceActivity extends AppCompatActivity implements View.OnClickListener{

    UserLocalStore userLocalStore;
    private EditText editTextComp, editTextTitle, editTextDescription;
    Button addWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_experience);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userLocalStore = new UserLocalStore(this);

        editTextComp = (EditText) findViewById(R.id.editTextCompany);
        editTextTitle = (EditText) findViewById(R.id.editTextJobTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextJobDescription);

        addWork = (Button) findViewById(R.id.buttonWorkExperience);
        addWork.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonWorkExperience:
                String company  = editTextComp.getText().toString();
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();

                User user = userLocalStore.getLoggedInUser();
                int uid = user.uid;

                User eUser = new User(uid, company, title, description);

                if (!validate()) {
                    return;
                }

                addWorkExp(eUser);
                break;


        }
    }

    public boolean validate() {
        boolean valid = true;

        String desc = editTextDescription.getText().toString();
        String title = editTextTitle.getText().toString();
        String comp = editTextComp.getText().toString();

        if (comp.isEmpty() || comp.length() < 3 || comp.length() > 30) {
            editTextComp.setError("between 3 and 30 characters");
            valid = false;
        } else {
            editTextComp.setError(null);
        }

        if (title.isEmpty() || title.length() < 3 || title.length() > 25) {
            editTextTitle.setError("between 3 and 25 characters");
            valid = false;
        } else {
            editTextTitle.setError(null);
        }

        if (desc.isEmpty() || desc.length() < 3 || desc.length() > 150) {
            editTextDescription.setError("between 3 and 150 characters");
            valid = false;
        } else {
            editTextDescription.setError(null);
        }

        return valid;
    }

    private void addWorkExp(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserWorkInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                Intent workPlease = new Intent(WorkExperienceActivity.this, MainActivity.class);
                workPlease.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(workPlease);
            }

            @Override
            public void donezo(String string) {

            }
        });
    }
}
