package com.example.messagesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Message> messages;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messages = new ArrayList<>();
        DataBaseHelper.initialize();
        DatabaseReference myReference = DataBaseHelper.getMyReference();

        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Message msg = d.getValue(Message.class);

                    List<Comment> comments = new ArrayList<>();
                    for(DataSnapshot commentShot : d.child("comment").getChildren()){
                        Comment comment = commentShot.getValue(Comment.class);
                        comments.add(comment);
                    }
                    Collections.sort(comments);
                    msg.setComment(comments);

                    msg.display();
                    messages.add(msg);
                }
                Collections.sort(messages);
                //QuickSortHelper.QuickSort(messages, 0, messages.size() - 1);
                DataBaseHelper.setMsg(messages);
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setHasFixedSize(true);
                myAdapter = new MyRecycleViewAdapter(MainActivity.this, messages);
                recyclerView.setAdapter(myAdapter);


                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipToDelete((MyRecycleViewAdapter)myAdapter));
                itemTouchHelper.attachToRecyclerView(recyclerView);

                ((MyRecycleViewAdapter)myAdapter).update();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void addMessage(String data){
        String key = DataBaseHelper.generateRandomKey();
        Message newMessage = new Message(data, key);
        messages.add(0, newMessage);
        DataBaseHelper.addMessage(newMessage);
        ((MyRecycleViewAdapter)myAdapter).update();
        return;
    }

    private void addMessage(){
        final EditText editText = new EditText(this);
        editText.setText("");
        new AlertDialog.Builder(this).setTitle("Add New Message")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(editText)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String data = editText.getText().toString();
                        addMessage(data);
                    }
                }).setNegativeButton("Cancel", null).show();
        return;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {
            case R.id.addMessage:
                addMessage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
