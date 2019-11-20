package kr.hs.emirim.dana.lookup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    private ArrayList<ItemData> nameData = null;
    private int nameCnt = 0;

    public ListAdapter(ArrayList<ItemData> nameData){
        this.nameData = nameData;
        nameCnt = this.nameData.size();
    }

    @Override
    public int getCount(){
        return nameCnt;
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            final Context context = parent.getContext();
            if (inflater == null){
                inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.nameList);
        name.setText(nameData.get(position).nameList);
        return convertView;
    }
}
