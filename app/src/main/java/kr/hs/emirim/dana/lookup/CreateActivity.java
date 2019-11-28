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
    TextView random_number;
    EditText room_name;
    EditText leader_name;
    Button hour_input;
    Button minute_input;
    Button create_bt;

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
    String rnd_code = Integer.toString(result);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        random_number = (TextView)findViewById(R.id.random_number);
        room_name = (EditText)findViewById(R.id.room_name);
        leader_name = (EditText)findViewById(R.id.leader_name);
        hour_input = (Button)findViewById(R.id.hour_input);
        hour_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        minute_input = (Button)findViewById(R.id.minute_input);
        minute_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        create_bt = (Button)findViewById(R.id.create_bt);
        create_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                name = room_name.getText().toString();
                owner = leader_name.getText().toString();
                timer = addZero(hour_input.getText().toString()) + addZero(minute_input.getText().toString());
                if (name != null || owner != null || !name.equals("") || !owner.equals("")) {
                    newGroupPost();
                    Intent intent = new Intent(CreateActivity.this, RoomActivity.class);
                    intent.putExtra("code", code);
                    intent.putExtra("name", owner);
                    startActivity(intent);

                }
            }
        });

        random_number.setText(rnd_code);
        code = rnd_code;

    }

    public String addZero(String s){
        if (s.length() < 2){
            return "0" + s;
        }
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