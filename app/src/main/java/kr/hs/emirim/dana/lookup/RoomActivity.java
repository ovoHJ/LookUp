package kr.hs.emirim.dana.lookup;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity {
    TextView textView;
    private ListView m_olistView = null;    //여기도 ========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

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
    }
}
