package kr.hs.emirim.dana.lookup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Group {

    String name;
    int code;
    String timer;
    String owner;

    public Group(){
        //Default constructor
    }

    public Group(String name, int code, String timer, String owner){
        name = this.name;
        code = this.code;
        timer = this.timer;
        owner = this.owner;
    }
}
