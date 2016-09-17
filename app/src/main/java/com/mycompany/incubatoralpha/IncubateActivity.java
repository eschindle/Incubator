package com.mycompany.incubatoralpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IncubateActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextIdeaN, editTextIdeaDesc;
    Button incubateButton;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incubate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userLocalStore = new UserLocalStore(this);

        editTextIdeaN = (EditText) findViewById(R.id.editTextIdeaName);
        editTextIdeaDesc = (EditText) findViewById(R.id.editTextIdeaDescription);

        incubateButton = (Button) findViewById(R.id.buttonIncubate);

        incubateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.buttonIncubate:
                String ideaName  = editTextIdeaN.getText().toString();
                String ideaDescription = editTextIdeaDesc.getText().toString();

                User user = userLocalStore.getLoggedInUser();
                int uid = user.uid;

                Idea idea = new Idea(uid, ideaName, ideaDescription);

                if (!validate()) {
                    return;
                }

                incubate(idea);
                break;
        }
    }

    public boolean validate() {
        boolean valid = true;

        String name = editTextIdeaN.getText().toString();
        String desc = editTextIdeaDesc.getText().toString();

        if (name.isEmpty() || name.length() < 3 || name.length() > 25) {
            editTextIdeaN.setError("between 3 and 25 alphanumeric characters");
            valid = false;
        } else {
            editTextIdeaN.setError(null);
        }

        if (desc.isEmpty() || desc.length() < 3 || desc.length() > 150) {
            editTextIdeaDesc.setError("between 3 and 150 alphanumeric characters");
            valid = false;
        } else {
            editTextIdeaDesc.setError(null);
        }

        return valid;
    }

    private void incubate(Idea idea){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeIdeaInBackground(idea, new GetIdeaCallback() {
            @Override
            public void done(Idea returnedIdea) {
                startActivity(new Intent(IncubateActivity.this, ideaActivity.class));
            }
        });
    }

}
