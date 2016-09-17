package com.mycompany.incubatoralpha;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class SignUpActivty extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextFName;
    private EditText editTextLName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextDob;

    private DatePickerDialog dobPickerDialog;
    private SimpleDateFormat dateFormatter;

    Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        findViewsById();
        setDateTimeField();

        editTextFName = (EditText) findViewById(R.id.firstNameEditText);
        editTextLName = (EditText) findViewById(R.id.lastNameEditText);
        editTextUsername = (EditText) findViewById(R.id.usernameEditText);
        editTextPassword = (EditText) findViewById(R.id.passwordEditText);
        editTextEmail = (EditText) findViewById(R.id.emailEditText);
        buttonRegister = (Button) findViewById(R.id.register);

        buttonRegister.setOnClickListener(this);
    }

    private void findViewsById() {
        editTextDob = (EditText) findViewById(R.id.dobEditText);
        editTextDob.setInputType(InputType.TYPE_NULL);
        editTextDob.requestFocus();
    }

    private void setDateTimeField() {
        editTextDob.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        dobPickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog,new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextDob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                String fName = editTextFName.getText().toString();
                String lName = editTextLName.getText().toString();
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String email = editTextEmail.getText().toString();
                String dob = editTextDob.getText().toString();

                User user = new User(fName, lName, username, password, email, dob);

                if (!validate()) {
                    return;
                }

                registerUser(user);
                break;

            case R.id.dobEditText:
                dobPickerDialog.show();
                break;
        }
    }

    public boolean validate() {
        boolean valid = true;

        String fName = editTextFName.getText().toString();
        String lName = editTextLName.getText().toString();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String email = editTextEmail.getText().toString();
        String dob = editTextDob.getText().toString();

        if (fName.isEmpty() || fName.length() < 2 || fName.length() > 20) {
            editTextFName.setError("between 2 and 20 alphanumeric characters");
            valid = false;
        } else {
            editTextFName.setError(null);
        }

        if (lName.isEmpty() || lName.length() < 2 || lName.length() > 20) {
            editTextLName.setError("between 2 and 20 alphanumeric characters");
            valid = false;
        } else {
            editTextLName.setError(null);
        }

        if (username.isEmpty() || username.length() < 3 || username.length() > 20) {
            editTextUsername.setError("between 3 and 20 alphanumeric characters");
            valid = false;
        } else {
            editTextUsername.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.length() < 3 || email.length() > 50) {
            editTextEmail.setError("enter a valid email address");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            editTextPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        if (dob.isEmpty()) {
            editTextDob.setError("Missing Date of Birth");
            valid = false;
        } else {
            editTextDob.setError(null);
        }

        return valid;
    }

    private void registerUser(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(SignUpActivty.this, LoginActivity.class));
            }

            @Override
            public void donezo(String string) {

            }
        });
    }
}