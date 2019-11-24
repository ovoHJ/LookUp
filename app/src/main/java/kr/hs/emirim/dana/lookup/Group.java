package kr.hs.emirim.dana.lookup;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group {

    String name;
    int personnel;
    int code;
    String timer;
    Map<String, String> member = new HashMap<>();

    public Group(){
        //Default constructor
    }

    public Group(String name, int code, int personnel, String timer, Map<String, String> member){
        this.name = name;
        this.code = code;
        this.personnel = personnel;
        this.timer = timer;
        if (timer == null)
            this.timer = "";
        this.member = member;
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
