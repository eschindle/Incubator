package com.mycompany.incubatoralpha;

public class Idea {

    int uid;
    int iid;
    int like;
    int ouid;
    String name, desc;

    public Idea(int uid, String name, String desc) {
        this.uid = uid;
        this.name = name;
        this.desc = desc;
    }

    public Idea(int uid, int iid, int like, int ouid){
        this.uid = uid;
        this.iid = iid;
        this.like = like;
        this.ouid = ouid;
    }
}
