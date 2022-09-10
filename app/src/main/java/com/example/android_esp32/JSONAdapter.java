package com.example.android_esp32;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class JSONAdapter extends ArrayAdapter<JSONfile> {
    public JSONAdapter(Context context, List<JSONfile> items) {
        super(context, 0, items);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONfile  item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_json, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.textView1);
        TextView sub1 = (TextView) convertView.findViewById(R.id.textView2);
        title.setText(item.getName());
        sub1.setText(item.getC());

        return convertView;
    }
}
