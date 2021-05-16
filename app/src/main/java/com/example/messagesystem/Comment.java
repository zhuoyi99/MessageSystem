package com.example.messagesystem;

import java.io.Serializable;

public class Comment implements Comparable, Serializable {
    private int numOfVote;
    private String data, key, parentKey;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String getKey(){ return key;}
    public String getParentKey(){ return parentKey;}
    public int getNumOfVote() {
        return numOfVote;
    }

    public void setKey(String k){ key = k;}
    public void setNumOfVote(int numOfVote) {
        this.numOfVote = numOfVote;
    }

    public Comment(){}
    public Comment(String d, int n){
        data = d;
        numOfVote = n;
    }
    public Comment(String d, int n, String k, String parent){
        data = d;
        numOfVote = n;
        key = k;
        this.parentKey = parent;
    }


    public void vote(){
        numOfVote += 1;
        return;
    }

    @Override
    public int compareTo(Object other) {
        return ((Comment)other).getNumOfVote() - numOfVote;
    }
}
