package com.mycompany.incubatoralpha;

public class User {
    int uid, ouid, like;
    String fName, lName, username, password, email, dob, school, major, grad, skill;

    public User(String fName, String lName, String username, String password, String email, String dob){
        this.fName = fName;
        this.lName = lName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.dob = dob;
        this.username = username;
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(int uid, String fName, String lName, String username, String password, String email, String dob){
        this.uid = uid;
        this.fName = fName;
        this.lName = lName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.dob = dob;
        this.username = username;
    }

    public User(int uid, String skill){
        this.uid = uid;
        this.skill = skill;
    }

    public User(int uid, String school, String major, String grad){
        this.uid = uid;
        this.school = school;
        this.major = major;
        this.grad = grad;
    }

    public User(int uid, int ouid, int like){
        this.uid = uid;
        this.ouid = ouid;
        this.like = like;
    }

}
