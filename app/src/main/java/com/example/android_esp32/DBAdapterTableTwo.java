package com.example.android_esp32;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;

public class DBAdapterTableTwo extends ArrayAdapter<DBTableTwo> {
    public DBAdapterTableTwo(Context context, List<DBTableTwo> items) {
        super(context, 0, items);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DBTableTwo  item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_json, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.textView1);
        TextView sub1 = (TextView) convertView.findViewById(R.id.textView2);
        title.setText("Element Name:"+item.getName());
        DataBaseAccess db=DataBaseAccess.getInstance(getContext());
        db.open();
        sub1.setText("Type:"+db.getName(item.getElement_Id()));
        db.close();

        return convertView;
    }
}