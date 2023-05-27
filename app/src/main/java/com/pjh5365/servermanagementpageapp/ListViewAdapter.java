package com.pjh5365.servermanagementpageapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    ArrayList<ListViewItem> arrayList = new ArrayList<>();
    Context context;

    public ListViewAdapter(Context context, ArrayList<ListViewItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void addItem(ListViewItem listViewItem) {
        arrayList.add(listViewItem);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.listview_item, null);

        TextView listNum = (TextView) view.findViewById(R.id.tv_listNum);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        TextView userID = (TextView) view.findViewById(R.id.tv_userID);

        listNum.setText(arrayList.get(position).getListNum());
        title.setText(arrayList.get(position).getTitle());
        userID.setText(arrayList.get(position).getUserID());

        return view;
    }
}
