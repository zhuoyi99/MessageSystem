package com.example.messagesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

public class MyListViewAdapter extends BaseAdapter {
    private List<Comment> comments;
    private Context con;
    public MyListViewAdapter(@NonNull Context context, List<Comment>c) {
        con = context;
        comments = c;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view;
        MyListViewHolder holder;
        Comment comment = comments.get(position);
        if(convertView == null){
            view = LayoutInflater.from(con).inflate(R.layout.list_view_item_for_comment, null, false);

            holder = new MyListViewHolder(view);
            view.setTag(holder);
        }
        else{
            view = convertView;
            holder = (MyListViewHolder)(view.getTag());
        }

        holder.textView.setText(comment.getData());
        holder.edit.setOnClickListener(new EditCommentClickListener(position));
        holder.vote.setOnClickListener(new VoteCommentClickListener(position));
        holder.delete.setOnClickListener(new DeleteCommentClickListener(position));
        holder.vote.setText(String.format("Vote\n(%d)", comments.get(position).getNumOfVote()));
        return view;
    }

    private void editComment(String data, int commentIndex){
        comments.get(commentIndex).setData(data);
        DataBaseHelper.editComment(comments.get(commentIndex));
        notifyDataSetChanged();
        return;
    }

    public void editCommentDialog(String text, final int commentIndex){
        final EditText editText = new EditText(con);
        editText.setText(text);
        new AlertDialog.Builder(con).setTitle("Edit Comment")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(editText)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String data = editText.getText().toString();
                        System.out.println("data=" + data);
                        editComment(data, commentIndex);
                    }
                }).setNegativeButton("Cancel", null).show();
        return;
    }
/*
    private void voteMsg(int msgIndex){
        Message msg = msgs.get(msgIndex);
        msg.increaseNumOfVote();

        DataBaseHelper.voteMessage(msg);
Collections.sort(msgs);
        notifyDataSetChanged();
        return;
    }
 */
    private void voteComment(int index){
        comments.get(index).vote();
        Collections.sort(comments);
        DataBaseHelper.editComment(comments.get(index));

        notifyDataSetChanged();
    }

    private void deleteComment(int index){
        Comment toBeDeleted = comments.get(index);
        comments.remove(index);
        DataBaseHelper.removeComment(toBeDeleted);
        notifyDataSetChanged();
        return;
    }

    class EditCommentClickListener implements View.OnClickListener {
        private int commentIndex;
        public EditCommentClickListener(int index){
            commentIndex = index;
        }
        @Override
        public void onClick(View v) {
            editCommentDialog(comments.get(commentIndex).getData(), commentIndex);
        }
    }

    class VoteCommentClickListener implements View.OnClickListener {
        private int commentIndex;
        public VoteCommentClickListener(int index){
            commentIndex = index;
        }
        @Override
        public void onClick(View v) {
            voteComment(commentIndex);
        }
    }

    class DeleteCommentClickListener implements View.OnClickListener {
        private int commentIndex;
        public DeleteCommentClickListener(int index){
            commentIndex = index;
        }
        @Override
        public void onClick(View v) {
            deleteComment(commentIndex);
        }
    }
}
