package kr.hs.emirim.dana.lookup;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.DialogFragment;

        import android.app.Dialog;
        import android.app.TimePickerDialog;
        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.LinearGradient;
        import android.graphics.Shader;
        import android.os.Bundle;
        import android.text.TextPaint;
        import android.text.format.DateFormat;
        import android.util.Log;
        import android.view.View;
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
    Button minute_input;
    Button create_bt;
    Spinner mode;

    private DatabaseReference mDatabase;
    DatabaseReference groupRef;
    String key;

    String name;
    int personnel = 0;
    String timer;
    String owner;
    Map<String, String> member = new HashMap();

    int min = 100000;
    int max = 999999;

    int result = (int) (Math.random() * (max - min + 1)) + min;
    String code = Integer.toString(result);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

       random_number = (TextView)findViewById(R.id.random_number);
//        room_name = (EditText)findViewById(R.id.room_name);
//        leader_name = (EditText)findViewById(R.id.leader_name);
//        hour_input = (Button)findViewById(R.id.hour_input);
//        minute_input = (Button)findViewById(R.id.minute_input);
//        create_bt = (Button)findViewById(R.id.create_bt);
        create_bt.setOnClickListener(m_crBtnClick);
        mode = (Spinner)findViewById(R.id.spinner); //모드가 타이머면 아래 요소 visible 바꾸기, 모드 값 RoomAcitivity로 넘기기

        random_number.setText(code);

        //코드 텍스트 색 바꾸기
        TextPaint paint = random_number.getPaint();
        float width = paint.measureText(code);

        Shader textShader = new LinearGradient(0, 0, width, random_number.getTextSize(),
                new int[]{
                        Color.parseColor("#00B2FF"),
                        Color.parseColor("#00CDC1"),
                }, null, Shader.TileMode.CLAMP);
        random_number.getPaint().setShader(textShader);

        //hour_input.setText(hour);
        //minute_input.setText(minute);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    Button.OnClickListener m_crBtnClick = new View.OnClickListener() {
        public void onClick(View v) {
            name = room_name.getText().toString();
            owner = leader_name.getText().toString();
            if (name != null && owner != null && !name.equals("") && !owner.equals("")) {
                newGroupPost();
                Intent intent = new Intent(CreateActivity.this, RoomActivity.class);
                intent.putExtra("code", code);
                startActivity(intent);

            }
        }
    };


    public void newGroupPost(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        groupRef = mDatabase.child("groups");
        Group group = new Group(name, ++personnel, timer, owner);

        Map<String, Object> postValues = group.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(code, postValues);

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