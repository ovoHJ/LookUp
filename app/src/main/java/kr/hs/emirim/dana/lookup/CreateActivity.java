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
    EditText hour_input;
    EditText minute_input;
    Button create_bt;

    private DatabaseReference mDatabase;
    String key;

    String name;
    String code;
    int personnel = 0;
    String timer;
    String owner;
    Map<String, String> member = new HashMap();

    int min = 100000;
    int max = 999999;

    int result = (int) (Math.random() * (max - min + 1)) + min;
    String rnd_code = Integer.toString(result);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.종료설정, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); spinner.setAdapter(adapter);


         random_number = (TextView)findViewById(R.id.random_number);
         room_name = (EditText)findViewById(R.id.room_name);
         leader_name = (EditText)findViewById(R.id.leader_name);

         hour_input = (EditText)findViewById(R.id.hour_input);
         minute_input = (EditText)findViewById(R.id.minute_input);

         create_bt = (Button)findViewById(R.id.create_bt);
         create_bt.setOnClickListener(m_crBtnClick);

         random_number.setText(rnd_code);
         code = rnd_code;


        final EditText hour_input = (EditText) findViewById(R.id.hour_input);
        final EditText minute_input = (EditText) findViewById(R.id.minute_input);

        hour_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(CreateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour_input.setText(selectedHour);
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker
               // mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        minute_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(CreateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        minute_input.setText(selectedMinute);
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker
               // mTimePicker.setTitle("Select Minute");
                mTimePicker.show();
            }
        });

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