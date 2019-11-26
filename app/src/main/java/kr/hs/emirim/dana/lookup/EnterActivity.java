package kr.hs.emirim.dana.lookup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EnterActivity extends AppCompatActivity {

    EditText input_code;
    EditText input_name;

    private DatabaseReference mDatabase;
    DatabaseReference memberRef;

    String code;
    String name;
    Map<String, Object> addMember = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        input_code = (EditText)findViewById(R.id.input_code);
        input_name = (EditText)findViewById(R.id.input_name);

        // input_code.setText();

        findViewById(R.id.start_bt).setOnClickListener(m_stBtnClick);
    }

    //enter room and add member
    public void enterRoom(){
        code = input_code.getText().toString();
        name = input_name.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        memberRef = mDatabase.child("groups").child(code).child("member");

        addMember.put(name, "user");
        memberRef.updateChildren(addMember);
    }//해당 코드가 DB 내에 있을 경우, 해당 코드의 데이터 키값을 받아서 member에 name : "user"형태로 추가


    Button.OnClickListener m_stBtnClick = new View.OnClickListener() {
        public void onClick(View v) {
            enterRoom();
            Intent intent = new Intent(EnterActivity.this, RoomActivity.class); //강은서 방접속한 후 들어가는 엑티비티 명 넣으셈.
            intent.putExtra("code", code);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    };

}
