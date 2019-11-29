package kr.hs.emirim.dana.lookup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    TextView random_number;
    EditText room_name;
    EditText leader_name;
    Button hour_input;
    TextView hour;
    Button minute_input;
    TextView minute;
    Button create_bt;
    Spinner spinner;
    String[] spn_items;
    ArrayAdapter<String> adapter;
    String mode;

    private DatabaseReference mDatabase;
    DatabaseReference groupRef;

    String name;
    String code;
    int personnel = 0;
    String timer;
    String owner;
    Map<String, String> member = new HashMap();

    int min = 100000;
    int max = 999999;

    int result = (int) (Math.random() * (max - min + 1)) + min;
    String rnd_code = Integer.toString(result); //코드 중복 방지

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        spn_items = getResources().getStringArray(R.array.종료설정);
        adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, spn_items);

        random_number = (TextView)findViewById(R.id.random_number);
        room_name = (EditText)findViewById(R.id.room_name);
        leader_name = (EditText)findViewById(R.id.leader_name);

        hour_input = (Button)findViewById(R.id.hour_input);
        hour = (TextView)findViewById(R.id.hour);
        minute_input = (Button)findViewById(R.id.minute_input);
        minute = (TextView)findViewById(R.id.minute);

        spinner = (Spinner)findViewById(R.id.spinner);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    mode = spinner.getSelectedItem().toString();

                    hour.setVisibility(View.VISIBLE);
                    minute.setVisibility(View.VISIBLE);
                    hour_input.setVisibility(View.VISIBLE);
                    minute_input.setVisibility(View.VISIBLE);

                    hour_input.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    minute_input.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
                else {
                    mode = spinner.getSelectedItem().toString();

                    hour.setVisibility(View.INVISIBLE);
                    minute.setVisibility(View.INVISIBLE);
                    hour_input.setVisibility(View.INVISIBLE);
                    minute_input.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        create_bt = (Button)findViewById(R.id.create_bt);
        create_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                name = room_name.getText().toString();
                owner = leader_name.getText().toString();

                timer = addZero(hour_input.getText().toString()) + " : " + addZero(minute_input.getText().toString());
                System.out.println("시간 > "+timer);
                if (name != null || owner != null || !name.equals("") || !owner.equals("")) {

                    newGroupPost();
                    Intent intent = new Intent(CreateActivity.this, RoomActivity.class);
                    intent.putExtra("code", code);
                    intent.putExtra("name", owner);
                    intent.putExtra("roomName", name);
                    intent.putExtra("mode", mode);
                    if(mode.equals("타이머")){
                        intent.putExtra("timer", timer);
                    }

                    startActivity(intent);

                }
            }
        });

        random_number.setText(rnd_code);
        code = rnd_code;

    }

    public String addZero(String s){
        if(s.length() == 0)
            s = "00";
        if(s.length() == 1)
            s = "0" + s;
        return s;
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public void newGroupPost(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        groupRef = mDatabase.child("groups");
        Group group = new Group(name, timer, owner);

        Map<String, Object> postValues = group.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(code, postValues);

        groupRef.updateChildren(childUpdates);
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