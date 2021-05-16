package com.example.messagesystem;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class MyListViewHolder {
    Button edit, vote, delete;
    TextView textView;

    public MyListViewHolder(@NonNull View v){
        edit = v.findViewById(R.id.edit);
        vote = v.findViewById(R.id.vote);
        delete = v.findViewById(R.id.delete);
        textView = v.findViewById(R.id.textView);
    }
}
