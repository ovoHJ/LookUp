package kr.hs.emirim.dana.lookup;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity {
    private ListView m_olistView = null;    //여기도 ========================================================
    TextView textView;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        code = intent.getExtras().getString("code");
        Log.d("code", code);

//여기부터 ====================================================================================================

        String[] nameData = {"강은서", "강지민", "강혜정", "원예린"};
        int nDatCnt = 0;
        ArrayList<ItemData> dnameData = new ArrayList<>();
        for (int i=0; i<nameData.length; i++){
            ItemData nameItem = new ItemData();
            nameItem.nameList = (i+1)+". "+nameData[i];
            dnameData.add(nameItem);
        }

        m_olistView = (ListView)findViewById(R.id.nameList);
        ListAdapter oAdapter = new ListAdapter(dnameData);
        m_olistView.setAdapter(oAdapter);
        textView = (TextView) findViewById(R.id.connectionCount);
        textView.setText(oAdapter.getCount()+"명");
//여기까지 ====================================================================================================

        textView = (TextView) findViewById(R.id.roomPwd);
        textView.setText("P0W2E3".toUpperCase());

        TextPaint paint = textView.getPaint();
        float width = paint.measureText("P0W2E3");

        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#00B2FF"),
                        Color.parseColor("#00CDC1"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);

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
                    Toast.makeText(getApplicationContext(), "Ok click", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton(" ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "No click", Toast.LENGTH_SHORT).show();
                }
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

}
