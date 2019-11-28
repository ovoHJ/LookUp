package kr.hs.emirim.dana.lookup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomActivity extends AppCompatActivity {

    private ListView rListView;
    List<ItemData> rArray = new ArrayList<ItemData>();
    TextView roomNameView;
    TextView roomPwdView;
    TextView roomCntView;

    private DatabaseReference mDatabase;
    DatabaseReference groupRef;
    DatabaseReference memberRef;
    AlertDialog.Builder builder;

    String code;
    String name;
    String own;
    String roomName;
    Map<String, Object> memberList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        code = intent.getExtras().getString("code");
        name = intent.getExtras().getString("name");
        roomName = intent.getExtras().getString("roomName");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        groupRef = mDatabase.child("groups").child(code);
        memberRef = groupRef.child("member");
        rListView = (ListView)findViewById(R.id.nameList);

        roomPwdView = (TextView) findViewById(R.id.roomPwd);
        roomPwdView.setText(code);
        roomNameView = (TextView) findViewById(R.id.roomName);
        roomNameView.setText(roomName);

        TextPaint paint = roomPwdView.getPaint();
        float width = paint.measureText(code);

        Shader textShader = new LinearGradient(0, 0, width, roomPwdView.getTextSize(),
                new int[]{
                        Color.parseColor("#00B2FF"),
                        Color.parseColor("#00CDC1"),
                }, null, Shader.TileMode.CLAMP);
        roomPwdView.getPaint().setShader(textShader);

        findViewById(R.id.floatingBtn).setOnClickListener(floatingBtnClick);


        memberRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                showList(new RoomActivity.MyCallback() {
                    @Override
                    public void onCallback(Map<String, Object> List) {
                        final ArrayList<String> namedata = new ArrayList<>();
                        memberList = (Map<String, Object>)List;
                        for (String key: memberList.keySet()) {
                            namedata.add(key);
                            if(key.equals(name)){
                                own = memberList.get(key).toString();
                            }
                        }
                        System.out.println("namedata : " + namedata);
                        int nDatCnt = 0;
                        final ArrayList<ItemData> dnameData = new ArrayList<>();

                        for (int i=0; i< namedata.size(); i++){
                            ItemData nameItem = new ItemData();
                            nameItem.nameList = namedata.get(i);
                            dnameData.add(nameItem);
                        }
                        System.out.println("dnameData : " + dnameData);

                        final ListAdapter rAdapter = new ListAdapter(dnameData);
                        rListView.setAdapter(rAdapter);

                        roomCntView = (TextView) findViewById(R.id.connectionCount);
                        roomCntView.setText(rAdapter.getCount()+"명");

                        rListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String list = (String)namedata.get(i);
                                if(name.equals(list)){
                                    showDialog();
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                showList(new RoomActivity.MyCallback() {
                    @Override
                    public void onCallback(Map<String, Object> List) {
                        final ArrayList<String> namedata = new ArrayList<>();
                        memberList = (Map<String, Object>)List;
                        for (String key: memberList.keySet()) {
                            namedata.add(key);
                            if(key.equals(name)){
                                own = memberList.get(key).toString();
                            }
                        }
                        int nDatCnt = 0;
                        final ArrayList<ItemData> dnameData = new ArrayList<>();
                        System.out.println("namedata : "+ namedata.toString());
                        for (int i=0; i< namedata.size(); i++){
                            ItemData nameItem = new ItemData();
                            nameItem.nameList = namedata.get(i);
                            dnameData.add(nameItem);
                        }

                        final ListAdapter rAdapter = new ListAdapter(dnameData);
                        rListView.setAdapter(rAdapter);

                        roomCntView = (TextView) findViewById(R.id.connectionCount);
                        roomCntView.setText(rAdapter.getCount()+"명");

                        rListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String list = (String)namedata.get(i);
                                if(name.equals(list)){
                                    showDialog();
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    Button.OnClickListener floatingBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDialog();
        }
    };

    public void showDialog(){
        builder = new AlertDialog.Builder(RoomActivity.this, R.style.customDialog);
        builder.setTitle("방을 나가시겠습니까?");

        builder.setPositiveButton(" ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                outOfRoom();
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

    public void outOfRoom(){
        if(own.equals("owner")) {
            memberRef.getParent().removeValue();
        } else {
            memberRef.child(name).removeValue();
        }
        Intent intent = new Intent(RoomActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 액티비티 스택에 쌓인 액티비티 제거
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //
        startActivity(intent);
        finish();
    }

    public void showList(final RoomActivity.MyCallback myCallback) {
             memberRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println(dataSnapshot);
                System.out.println("key : " + dataSnapshot.getKey());
                System.out.println("value : " + dataSnapshot.getValue());
                memberList.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                System.out.println(memberList);
                myCallback.onCallback(memberList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().toString().equals("member")){
                    Map<String, Object> membernames = (Map<String, Object>)dataSnapshot.getValue();
                    for(String membersKey : membernames.keySet()){
                        memberList.put(membersKey, membernames.get(membersKey));
                    }
                }
                myCallback.onCallback(memberList);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface MyCallback {
        void onCallback(Map<String, Object> memberList);
    }

}
