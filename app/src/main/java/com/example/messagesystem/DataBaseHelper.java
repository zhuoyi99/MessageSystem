package com.example.messagesystem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBaseHelper {
    private static FirebaseDatabase fireBase;
    private static DatabaseReference myReference;
    private static List<Message> msgs;
    public static void initialize(){
        fireBase = FirebaseDatabase.getInstance();
        myReference = fireBase.getReference("Message");
    }

    public static void setMsg(List<Message>m){
        msgs = m;
        return;
    }

    public static DatabaseReference getMyReference(){
        return myReference;
    }

    public static String generateRandomKey(){
        return myReference.push().getKey();
    }

    public static void addMessage(Message newMessage){
        myReference.child(newMessage.getKey()).setValue(newMessage);
        return;
    }

    public static void voteMessage(Message msg){
        myReference.child(msg.getKey()).setValue(msg);
        return;
    }

    public static void removeMessage(Message msg){
        myReference.child(msg.getKey()).removeValue();
        return;
    }

    public static void editMessage(Message newMessage){
        myReference.child(newMessage.getKey()).setValue(newMessage);
        return;
    }

    public static Message findMessageByKey(String key){
        for(Message msg : msgs){
            System.out.println("msg key = " + msg.getKey() + " wanted key = " + key);
            if(msg.getKey().equals(key)){
                return msg;
            }
        }
        return null;
    }

    public static void addComment(Comment comment){
        Message associatedMessage = findMessageByKey(comment.getParentKey());
        editMessage(associatedMessage);
        return;
    }

    public static void removeComment(Comment comment){
        Message associatedMessage = findMessageByKey(comment.getParentKey());
        editMessage(associatedMessage);
        return;
    }

    public static void editComment(Comment comment){
        Message associatedMessage = findMessageByKey(comment.getParentKey());
        editMessage(associatedMessage);
        return;
    }
}
