package com.example.ednei.gravadorv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

public class MyList extends BaseAdapter {

    private File[] files = ReadFilesPath.read();
    private Context context;

    public MyList(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public Object getItem(int position) {
        return files[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String f = files[position].getName().toString();
        View view = LayoutInflater.from(context).inflate(R.layout.main_list, parent, false);
        TextView t = (TextView) view.findViewById(R.id.textViewFiles);
        t.setText(f);

        return view;
    }
}
