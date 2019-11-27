package kr.hs.emirim.dana.lookup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    private ListView rListView;
    List<ItemData> rArray = new ArrayList<ItemData>();
    TextView rTextView;

    private DatabaseReference mDatabase;
    DatabaseReference groupRef;
    ChildEventListener childEventListener;

    String code;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        code = intent.getExtras().getString("code");
        name = intent.getExtras().getString("name");
        System.out.println(code + ", " + name);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        groupRef = mDatabase.child("groups").child(code);
        rListView = (ListView)findViewById(R.id.nameList);

//=====================================================================================
        String[] nameData = {"강은서", "강지민", "강혜정", "원예린"};
        int nDatCnt = 0;
        final ArrayList<ItemData> dnameData = new ArrayList<>();
        for (int i=0; i<nameData.length; i++){
            ItemData nameItem = new ItemData();
            nameItem.nameList = (i+1)+". "+nameData[i];
            dnameData.add(nameItem);
        }
//=====================================================================================

        ListAdapter rAdapter = new ListAdapter(dnameData);
        rListView.setAdapter(rAdapter);

        rArray.add(new ItemData());

        rTextView = (TextView) findViewById(R.id.connectionCount);
        rTextView.setText(rAdapter.getCount()+"명");

        rListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(RoomActivity.this, dnameData.get(i).getNameList(), Toast.LENGTH_SHORT).show();
            }
        });
//=====================================================================================

        rTextView = (TextView) findViewById(R.id.roomPwd);
        rTextView.setText(code);

        TextPaint paint = rTextView.getPaint();
        float width = paint.measureText(code);

        Shader textShader = new LinearGradient(0, 0, width, rTextView.getTextSize(),
                new int[]{
                        Color.parseColor("#00B2FF"),
                        Color.parseColor("#00CDC1"),
                }, null, Shader.TileMode.CLAMP);
        rTextView.getPaint().setShader(textShader);

        findViewById(R.id.floatingBtn).setOnClickListener(floatingBtnClick);
    }

    Button.OnClickListener floatingBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RoomActivity.this, R.style.customDialog);
            builder.setTitle("방을 나가시겠습니까?");

            builder.setPositiveButton(" ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    groupRef.removeValue();
                    Intent intent = new Intent(RoomActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 액티비티 스택에 쌓인 액티비티 제거
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //
                    startActivity(intent);
                    finish();
                }
            });

            builder.setNegativeButton(" ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            Button yes = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Drawable img1 = ContextCompat.getDrawable(RoomActivity.this,R.drawable.yes);
            img1.setBounds(0, 0, 70, 70);
            yes.setCompoundDrawables(img1, null, null, null);

            Button no = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            Drawable img2 = ContextCompat.getDrawable(RoomActivity.this,R.drawable.exit);
            img2.setBounds(0, 0, 70, 70);
            no.setCompoundDrawables(img2, null, null, null);
        }
    };

    public void showList(){
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Group group = dataSnapshot.getValue(Group.class);
                Log.d("member", group.name);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        groupRef.addChildEventListener(childEventListener);
    }

    public void removeGroup(){
        groupRef.removeValue();
        Log.d("remove test", "this is successed too");
    }

}
