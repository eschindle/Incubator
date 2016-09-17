package com.mycompany.incubatoralpha;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }


    public static String TAG_JSON_ARRAY="result";
    public static String TAG_companyName = "companyName";
    public static String TAG_jobTitle = "jobTitle";
    public static String TAG_jobDescription = "jobDescription";
    public static String TAG_schoolName = "schoolName";
    public static String TAG_major = "major";
    public static String TAG_gradYear = "gradYear";
    public static String TAG_skillName = "skillName";

    public static String TAG_ideaName = "ideaName";
    public static String TAG_desc = "ideaDesc";

    public static String TAG_otherIdeaID = "ideaID";
    public static String TAG_otherIdeaName = "ideaName";
    public static String TAG_otherDesc = "ideaDesc";
    public static String TAG_otherUID = "ideaUID";

    public static String TAG_connectFname = "fname";
    public static String TAG_connectLname = "lname";
    public static String TAG_connectUID = "uid";

    public static String TAG_contactFname = "fname";
    public static String TAG_contactLname = "lname";
    public static String TAG_contactUID = "uid";


    public void storeUserAttributes(String s){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("TAG_JSON_ARRAY", s);
        spEditor.apply();
    }

    public void storeUserConnect(String s){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("CONNECT_ARRAY", s);
        spEditor.apply();
    }

    public void storeUserContacts(String s){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("CONTACT_ARRAY", s);
        spEditor.apply();
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putInt("uid", user.uid);
        spEditor.putString("fName", user.fName);
        spEditor.putString("lName", user.lName);
        spEditor.putString("username", user.username);
        spEditor.putString("password", user.password);
        spEditor.putString("email", user.password);
        spEditor.putString("dob", user.dob);
        spEditor.apply();
    }

    public void storeIdeaData(String s) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("IDEA_ARRAY",s);
        spEditor.apply();
    }

    public void storeOtherIdeaData(String s) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("IDEA_ARRAYO",s);
        spEditor.apply();
    }

    public String getIdeaData() {
        return userLocalDatabase.getString("IDEA_ARRAY", "");
    }

    public String getContacts() {
        return userLocalDatabase.getString("CONTACT_ARRAY", "");
    }

    public String getOtherIdeaData() {
        return userLocalDatabase.getString("IDEA_ARRAYO", "");
    }

    public String getUserConnectData() {
        return userLocalDatabase.getString("CONNECT_ARRAY", "");
    }

    public User getLoggedInUser() {
        int uid = userLocalDatabase.getInt("uid", -1);
        String fName = userLocalDatabase.getString("fName", "");
        String lName = userLocalDatabase.getString("lName", "");
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");
        String email = userLocalDatabase.getString("email", "");
        String dob = userLocalDatabase.getString("dob", "");

        return new User(uid, fName, lName, username, password, email, dob);
    }

    public String getAttributes() {
        return userLocalDatabase.getString("TAG_JSON_ARRAY","");
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.apply();
    }

    public boolean getUserLoggedIn(){
        return userLocalDatabase.getBoolean("loggedIn", false);
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor  = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
    }
}
