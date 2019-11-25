package kr.hs.emirim.dana.lookup;

        import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.util.Log;

        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.HashMap;
        import java.util.Map;

public class CreateActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String key = "";

    String name = "lookup_developers";
    int code = 123459;
    int personnel = 0;
    String timer = null;
    String nickname = "원예린";
//    위 5줄은 테스트 용
    Map<String, String> member = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        if (nickname == null){
            nickname = "";
        }
        member.put(nickname, "owner");
        newPost();
        //roomActivity로 넘어가기 (key값 넘기기)
    }

    public void newPost(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        key = mDatabase.child("groups").push().getKey();
        Group group = new Group(name, code, personnel, timer, member);

        Map<String, Object> postValues = group.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/groups/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

//    public void removeGroup(){
//        mDatabase.child("groups").child(key).removeValue();
//        Log.d("remove test", "this is successed too");
//    }
//     remove test (원래 roomActiviry에 있을 메서드)
}
