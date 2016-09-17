package com.mycompany.incubatoralpha;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EducationActivity extends AppCompatActivity implements View.OnClickListener {

    UserLocalStore userLocalStore;

    private EditText editTextSchool, editTextMajor, editTextGrad;
    private DatePickerDialog dobPickerDialog;
    private SimpleDateFormat dateFormatter;
    Button addEducation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        findViewsById();
        setDateTimeField();

        editTextSchool = (EditText) findViewById(R.id.editTextSchool);
        editTextMajor = (EditText) findViewById(R.id.editTextMajor);
        editTextGrad = (EditText) findViewById(R.id.editTextGrad);
        userLocalStore = new UserLocalStore(this);

        addEducation = (Button) findViewById(R.id.buttonAddEducation);
        addEducation.setOnClickListener(this);
    }

    private void findViewsById() {
        editTextGrad = (EditText) findViewById(R.id.editTextGrad);
        editTextGrad.setInputType(InputType.TYPE_NULL);
        editTextGrad.requestFocus();
    }

    private void setDateTimeField() {
        editTextGrad.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        dobPickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog,new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextGrad.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAddEducation:

                String school = editTextSchool.getText().toString();
                String major = editTextMajor.getText().toString();
                String grad = editTextGrad.getText().toString();

                User user = userLocalStore.getLoggedInUser();
                int uid = user.uid;

                User eUser = new User(uid, school, major, grad);

                if (!validate()) {
                    return;
                }

                addEduc(eUser);
                break;
            case R.id.editTextGrad:
                dobPickerDialog.show();
                break;
        }
    }

    public boolean validate() {
        boolean valid = true;

        String grad = editTextGrad.getText().toString();
        String major = editTextMajor.getText().toString();
        String school = editTextSchool.getText().toString();

        if (school.isEmpty() || school.length() < 3 || school.length() > 50) {
            editTextSchool.setError("between 3 and 50 alphanumeric characters");
            valid = false;
        } else {
            editTextSchool.setError(null);
        }

        if (major.isEmpty() || major.length() < 3 || major.length() > 25) {
            editTextMajor.setError("between 3 and 25 alphanumeric characters");
            valid = false;
        } else {
            editTextMajor.setError(null);
        }

        if (grad.isEmpty()) {
            editTextGrad.setError("Please fill in the Year");
            valid = false;
        } else {
            editTextGrad.setError(null);
        }

        return valid;
    }

    private void addEduc(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserEducationInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(EducationActivity.this, MainActivity.class));

            }

            @Override
            public void donezo(String string) {

            }
        });
    }
}
