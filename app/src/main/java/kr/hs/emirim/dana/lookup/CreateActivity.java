package kr.hs.emirim.dana.lookup;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {

    private TextView textView_Date;
    private TimePickerDialog.OnTimeSetListener callbackMethod;

    private DatabaseReference mDatabase;
    String key;

    String name;
    String code;
    int personnel = 0;

    String timer = null;
    String nickname = "원예린";
    //    위 5줄은 테스트 용
    Map<String, String> member = new HashMap();

    int min = 100000;
    int max = 999999;

    int result = (int) (Math.random() * (max - min + 1)) + min;
    String rnd_code = Integer.toString(result);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        TextView random_number = (TextView)findViewById(R.id.random_number);
        random_number.setText(rnd_code);

        findViewById(R.id.create_bt).setOnClickListener(m_crBtnClick);

        EditText room_name = (EditText)findViewById(R.id.room_name);
        EditText leader_name = (EditText)findViewById(R.id.leader_name);

        Button hour_input = (Button)findViewById(R.id.hour_input);
        Button minute_input = (Button)findViewById(R.id.minute_input);

        //hour_input.setText(hour);
        //minute_input.setText(minute);

        if (nickname == null){
            nickname = "";
        }
        member.put(nickname, "owner");
        newPost();
        //roomActivity로 넘어가기 (key값 넘기기)
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    Button.OnClickListener m_crBtnClick = new View.OnClickListener() {
        public void onClick(View v) {
            name = room_name.getText().toString();
            owner = leader_name.getText().toString();
            if (name != null || owner != null || !name.equals("") || !owner.equals("")) {
                newPost();
                Intent intent = new Intent(CreateActivity.this, RoomActivity.class);
                startActivity(intent);

            }
        }
    };


    public void newPost(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference groupRef = mDatabase.child("groups");
        key = mDatabase.child("groups").push().getKey();
        Log.d("name", name);
        Log.d("owner", owner);
        Group group = new Group(name, code, ++personnel, timer, owner);

        Map<String, Object> postValues = group.toMap();
        for (String mapkey : postValues.keySet()){
            System.out.println("key:"+mapkey+",value:"+postValues.get(mapkey));
        }

        Map<String, Object> childUpdates = new HashMap<>();
        Log.d("잘못된 경로", key);
        childUpdates.put(key, postValues);
        System.out.println("key : " + key + ", value : " + childUpdates.get(key));

        groupRef.updateChildren(childUpdates);
    }

//    public void removeGroup(){
//        mDatabase.child("groups").child(key).removeValue();
//        Log.d("remove test", "this is successed too");
//    }
//     remove test (원래 roomActiviry에 있을 메서드)
//    }

    int min = 100000;
    int max = 999999;

    int result = (int) (Math.random() * (max - min + 1)) + min;
    String rnd_code = Integer.toString(result);


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    Button.OnClickListener m_crBtnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(CreateActivity.this, EnterActivity.class);
            startActivity(intent);
        }
    };

}

class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }


}

class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}