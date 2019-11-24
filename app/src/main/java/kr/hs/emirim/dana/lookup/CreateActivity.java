package kr.hs.emirim.dana.lookup;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.util.Log;

        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.ArrayList;
        import java.util.Collection;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.Set;

public class CreateActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    String name;
    int code;
    String timer;
    String nickname;
    Map<String, String> member = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        name = "lookup_developers";
        code = 123458;
        timer = null;
        member.put(nickname, "owner");

        newPost();
    }

    public void newPost(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("groups").push().getKey();
        Group group = new Group(name, code, timer, member);
        System.out.println(group.name);
        Map<String, Object> postValues = group.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/groups/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

}
