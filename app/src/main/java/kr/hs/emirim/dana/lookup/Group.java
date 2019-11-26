package kr.hs.emirim.dana.lookup;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Group {

    String name;
    int personnel;
    String timer;
    String owner;
    Map<String, String> member = new HashMap<>();

    public Group(){
        //Default constructor
    }

    public Group(String name, int personnel, String timer, String owner){

        if (name == null)
            this.name = "";
        this.name = name;
        this.personnel = personnel;
        this.timer = timer;
        if (timer == null)
            this.timer = "";
        this.owner = owner;
        this.member.put(this.owner, "owner");

        System.out.println(name + " " + personnel + " " + timer + " " + member);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("personnel", personnel);
        result.put("timer", timer);
        result.put("member", member);

        return result;
    }
}
