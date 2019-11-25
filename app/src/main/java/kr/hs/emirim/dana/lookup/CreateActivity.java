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

import java.util.Calendar;

public class CreateActivity extends AppCompatActivity {

    private TextView textView_Date;
    private TimePickerDialog.OnTimeSetListener callbackMethod;


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
    }

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
