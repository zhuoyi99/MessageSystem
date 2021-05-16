package com.example.messagesystem;

import com.example.messagesystem.Comment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message implements Serializable, Comparable {
    private String msg, key;
    private List<Comment> comments;
    private int numOfVote;

    public Message(){}
    public Message(String m) {
        msg = m;
        comments = new ArrayList<>();
        numOfVote = 0;
    }
    public Message(String m, String k) {
        msg = m;
        key = k;
        comments = new ArrayList<>();
        numOfVote = 0;
    }

    public void setComment(List<Comment>c){comments = c;}
    public String getKey(){ return key; }
    public String getMsg(){
        return msg;
    }
    public void setMsg(String data){msg = data;}
    public int getNumOfVote(){return numOfVote;}
    public void increaseNumOfVote(){numOfVote += 1;}

    public void display(){
        System.out.println("key=" + key);
        System.out.println("msg=" + msg);
        System.out.println("commment count = " + comments.size());
        for(Comment c : comments){
            System.out.println("data="+ c.getData() + " parent-key=" + c.getParentKey());
        }
    }

    public void setKey(String k){key = k;}
    public List<Comment> getComment(){
        return comments;
    }

    public void addComment(Comment comment){
        comments.add(comment);
        sortCommentByVote();
        return;
    }

    public void sortCommentByVote(){
        Collections.sort(comments);
        return;
    }

    @Override
    public int compareTo(Object o) {
        Message other = (Message)o;
        return other.getNumOfVote() - numOfVote;
    }
}
