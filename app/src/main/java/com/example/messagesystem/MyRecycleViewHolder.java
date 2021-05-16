package com.example.messagesystem;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecycleViewHolder extends RecyclerView.ViewHolder {

    TextView textView;
    Button edit, comment, vote;
    ListView commentListView;

    public MyRecycleViewHolder(@NonNull View v) {
        super(v);
        textView = v.findViewById(R.id.textView);
        edit = v.findViewById(R.id.edit);
        commentListView = v.findViewById(R.id.listView);
        comment = v.findViewById(R.id.comment);
        vote = v.findViewById(R.id.vote);
    }
}
