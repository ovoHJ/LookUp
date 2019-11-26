package kr.hs.emirim.dana.lookup;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Group {

    String name;
    int personnel;
    String code;
    String timer;
    String owner;
    Map<String, String> member = new HashMap<>();

    public Group(){
        //Default constructor
    }

    public Group(String name, String code, int personnel, String timer, String owner){

        if (name == null)
            this.name = "";
        this.name = name;
        this.code = code;
        this.personnel = personnel;
        this.timer = timer;
        if (timer == null)
            this.timer = "";
        this.owner = owner;
        System.out.println(name);
        System.out.println(code);
        this.member.put(this.owner, "owner");

        System.out.println(name + " " + code + " " + personnel + " " + timer + " " + member);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("code", code);
        result.put("personnel", personnel);
        result.put("timer", timer);
        result.put("member", member);

        return result;
    }
}
