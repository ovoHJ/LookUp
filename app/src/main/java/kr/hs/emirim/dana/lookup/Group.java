package kr.hs.emirim.dana.lookup;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group {

    String name;
    int code;
    String timer;
    Map<String, String> member;

    public Group(){
        //Default constructor
    }

    public Group(String name, int code, String timer, Map<String, String> member){
        this.name = name;
        this.code = code;
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
        result.put("timer", timer);
        result.put("member", member);

        return result;
    }
}
