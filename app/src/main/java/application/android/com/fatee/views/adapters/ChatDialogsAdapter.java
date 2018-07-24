package com.example.sang.chattingdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class ChatDialogsAdapter extends BaseAdapter {
    ArrayList<QBChatDialog> qbChatDialogs;
    Context context;

    public ChatDialogsAdapter(ArrayList<QBChatDialog> qbChatDialogs, Context context) {
        this.qbChatDialogs = qbChatDialogs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return qbChatDialogs.size();
    }

    @Override
    public Object getItem(int i) {
        return qbChatDialogs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (convertView == null) {
        view = LayoutInflater.from(context).inflate(R.layout.chatdialog, viewGroup, false);
        TextView tvTitle, tvMessage;
        tvTitle = (TextView) view.findViewById(R.id.inflateTitle);
        tvMessage = (TextView) view.findViewById(R.id.inflateMessage);
        tvTitle.setText(qbChatDialogs.get(i).getName());
        tvMessage.setText(qbChatDialogs.get(i).getLastMessage());
       }

        return view;
    }

}
