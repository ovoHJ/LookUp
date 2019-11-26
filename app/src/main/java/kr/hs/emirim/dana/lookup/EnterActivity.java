package kr.hs.emirim.dana.lookup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        EditText input_code = (EditText)findViewById(R.id.input_code);
        EditText input_name = (EditText)findViewById(R.id.input_name);

        // input_code.setText();

        findViewById(R.id.start_bt).setOnClickListener(m_stBtnClick);
    }

    Button.OnClickListener m_stBtnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(EnterActivity.this, RoomActivity.class); //강은서 방접속한 후 들어가는 엑티비티 명 넣으셈.
            startActivity(intent);
        }
    };

}
