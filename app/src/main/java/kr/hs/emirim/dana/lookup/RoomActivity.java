package kr.hs.emirim.dana.lookup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoomActivity extends AppCompatActivity {

    private ListView rListView;
    List<ItemData> rArray = new ArrayList<ItemData>();
    TextView roomNameView;
    TextView roomView;
    TextView roomPwdView;
    TextView roomCntView;

    private DatabaseReference mDatabase;
    DatabaseReference groupRef;
    DatabaseReference memberRef;
    AlertDialog.Builder builder;
    ListAdapter rAdapter;

    String code;
    String name;
    String own;
    String roomName;
    String choice;

    String mode;
    String timer;
    int totalTime;
    int master;
    FloatingActionButton fab;
    Map<String, Object> memberList;

    ArrayList<String> namedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_room);



        fab = (FloatingActionButton) findViewById(R.id.floatingBtn);
        memberList = new LinkedHashMap<>();

        Intent intent = getIntent();
        code = intent.getExtras().getString("code");
        name = intent.getExtras().getString("name");
        roomName = intent.getExtras().getString("roomName");
        mode = intent.getExtras().getString("mode");
        master = intent.getExtras().getInt("master");

        if (master == 0 && mode.equals("사용자 지정")) {
            findViewById(R.id.floatingBtn).setVisibility(View.INVISIBLE);
        }

        if (mode.equals("타이머")) {
            timer = intent.getExtras().getString("timer");

            fab.setImageResource(R.drawable.clock);
        }

        fab.setOnClickListener(floatingBtnClick);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        groupRef = mDatabase.child("groups").child(code);
        memberRef = groupRef.child("member");
        rListView = (ListView) findViewById(R.id.nameList);

        rListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choice = (String) namedata.get(i);
                if (master == 1) {
                    if (!name.equals(choice))
                        exitDialog();
                    else
                        showDialog();
                } else {
                    if (name.equals(choice))
                        view.findViewById(R.id.statusCircle).setBackgroundResource(R.drawable.draw_circle_me);
                }
            }
        });

        roomPwdView = (TextView) findViewById(R.id.roomPwd);
        roomPwdView.setText(code);
        roomNameView = (TextView) findViewById(R.id.roomName);
        roomNameView.setText(roomName);
        roomView = (TextView) findViewById(R.id.roomView);
        roomView.setText(roomName.substring(0, 1));

        if (mode.equals("타이머")) {
            String t = intent.getExtras().getString("timer");
            if (t.equals("")) {
                t = "종료 시간 / 00 : 00";
            }
            int hour = Integer.parseInt(t.substring(8, 9 + 1));
            int min = Integer.parseInt(t.substring(t.length() - 2));
            totalTime = hour * 3600 + min * 60;
            //timer = minute;

            final CountDownTimer timers = new CountDownTimer(totalTime * 1000, 1000) {
                @Override
                public void onFinish() {
                    timer = "종료";
                    outOfRoom();
                }

                @Override
                public void onTick(long millisUntilFinished) {
                    totalTime -= 1;

                    int h = totalTime / 3600;
                    int m = (totalTime % 3600) / 60;
                    int s = ((totalTime % 3600) % 60) / 60;

                    if (totalTime >= 60) {
                        timer = Integer.toString(h) + "시간 " + Integer.toString(m) + "분 " + Integer.toString(s) + "초";
                    } else if (totalTime < 60) {
                        timer = "1분 미만";
                    }
                }
            };
            timers.start();
            fab.setImageResource(R.drawable.clock);
        }
        fab.setOnClickListener(floatingBtnClick);

        TextPaint paint = roomPwdView.getPaint();
        float width = paint.measureText(code);

        Shader textShader = new LinearGradient(0, 0, width, roomPwdView.getTextSize(),
                new int[]{
                        Color.parseColor("#00B2FF"),
                        Color.parseColor("#00CDC1"),
                }, null, Shader.TileMode.CLAMP);
        roomPwdView.getPaint().setShader(textShader);


        memberRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                memberList.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                namedata = new ArrayList<>();
                for (String key : memberList.keySet()) {
                    namedata.add(key);
                    if (key.equals(name)) {
                        if (memberList.get(key) != null)
                            own = memberList.get(key).toString();
                    }
                }
                showListView();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                namedata = new ArrayList<>();

                memberList.remove(dataSnapshot.getKey(), dataSnapshot.getValue());

                for (String key : memberList.keySet()) {
                    namedata.add(key);
                }

                if ((master != 1) && memberList.containsValue("owner") && !(memberList.containsValue(memberRef.child(name).getKey()))) {
                    initActivity();
                }

                if (!(memberList.containsValue("owner") && (!(memberList.toString().equals("{}"))) || rAdapter.getCount() == 0)) {
                    initActivity();
                }
                showListView();
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
            if (mode.equals("타이머")) {
                Toast toast = Toast.makeText(RoomActivity.this, timer, Toast.LENGTH_SHORT);
                View toastView = toast.getView(); // This'll return the default View of the Toast.

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setWidth(500);
                toastMessage.setTextSize(20);
                toastMessage.setTextColor(Color.WHITE);
                toastMessage.setTypeface(Typeface.create("roboto_bold", Typeface.BOLD));
                toastMessage.setGravity(Gravity.CENTER);
                toastView.setBackgroundResource(R.drawable.bg_gradient);
                toast.setGravity(Gravity.BOTTOM, -100,
                        getApplicationContext().getResources().getDisplayMetrics().heightPixels * 9 / 10 - fab.getTop());
                toast.show();
            } else {
                showDialog();
            }
        }
    };

    public void showDialog() {
        builder = new AlertDialog.Builder(RoomActivity.this, R.style.customDialog);
        if (master == 1)
            builder.setTitle("방을 없애시겠습니까?");
        else
            builder.setTitle("방을 나가시겠습니까?");

        builder.setPositiveButton(" ", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                outOfRoom();
            }
        });

        builderSetting();
    }

    public void exitDialog() {
        builder = new AlertDialog.Builder(RoomActivity.this, R.style.customDialog);
        builder.setTitle("선택한 팀원 내보내기");

        builder.setPositiveButton(" ", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                exitOfRoom(choice);
            }
        });

        builderSetting();
    }

    public void builderSetting() {
        builder.setNegativeButton(" ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Button yes = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Drawable img1 = ContextCompat.getDrawable(RoomActivity.this, R.drawable.yes);
        img1.setBounds(70, 0, 140, 70);
        yes.setCompoundDrawables(img1, null, null, null);

        Button no = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Drawable img2 = ContextCompat.getDrawable(RoomActivity.this, R.drawable.exit);
        img2.setBounds(70, 0, 140, 70);
        no.setCompoundDrawables(img2, null, null, null);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) yes.getLayoutParams();
        layoutParams.weight = 10;
        yes.setLayoutParams(layoutParams);
        no.setLayoutParams(layoutParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void outOfRoom() {
        if (own.equals("owner")) {
            memberRef.getParent().removeValue();
        } else {
            memberRef.child(name).removeValue();
            memberList = new LinkedHashMap<>();
        }
        initActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void exitOfRoom(String name) {
        memberRef.child(name).removeValue();
    }

    public void initActivity() {
        Intent intent = new Intent(RoomActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 액티비티 스택에 쌓인 액티비티 제거
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n") //하드코딩 된 스트링 사용 때문에 추가
    public void showListView() {
        final ArrayList<ItemData> dnameData = new ArrayList<>();

        for (int i = 0; i < namedata.size(); i++) {
            ItemData nameItem = new ItemData();
            nameItem.nameList = namedata.get(i);
            dnameData.add(nameItem);
        }

        rAdapter = new ListAdapter(dnameData);
        rListView.setAdapter(rAdapter);

        roomCntView = (TextView) findViewById(R.id.connectionCount);
        roomCntView.setText(rAdapter.getCount() + "명");

    }



    // 백버튼
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}


