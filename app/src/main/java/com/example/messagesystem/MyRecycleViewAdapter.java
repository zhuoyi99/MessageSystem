package com.example.messagesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyRecycleViewAdapter extends RecyclerView.Adapter {
    private List<Message> msgs;
    private Context context;

    public MyRecycleViewAdapter(Context con, List<Message> messages){
        context = con;
        msgs = messages;
    }

    public void update(){
        notifyDataSetChanged();
        return;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_recycle_view_item, parent, false);
        MyRecycleViewHolder holder = new MyRecycleViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyRecycleViewHolder holder = (MyRecycleViewHolder)viewHolder;
        holder.textView.setText(msgs.get(position).getMsg());
        holder.vote.setText(String.format("Vote\n(%d)", msgs.get(position).getNumOfVote()));
        holder.edit.setOnClickListener(new EditMessageClickListener(position));
        holder.comment.setOnClickListener(new AddCommentClickListener(position));
        holder.vote.setOnClickListener(new VoteMessageClickListener(position));

        MyListViewAdapter listViewAdapter = new MyListViewAdapter(context, msgs.get(position).getComment());
        holder.commentListView.setAdapter(listViewAdapter);

        fixListViewHeight(holder.commentListView);
        return;
    }

    public void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listViewItem = listAdapter.getView(i , null, listView);
            // 计算子项View 的宽高
            listViewItem.findViewById(R.id.textView).measure(0, 0);
            listViewItem.measure(0, 0);
            // 计算所有子项的高度和
            int measureHeight = listViewItem.getMeasuredHeight();
            totalHeight += measureHeight;
            System.out.println("ListView Item height = " + measureHeight);
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    public void deleteItem(int position){
        DataBaseHelper.removeMessage(msgs.get(position));
        msgs.remove(position);
        notifyDataSetChanged();
        return;
    }

    public void editMsg(String data, int msgIndex){
        msgs.get(msgIndex).setMsg(data);
        DataBaseHelper.editMessage(msgs.get(msgIndex));
        notifyDataSetChanged();
        return;
    }

    private void addComment(String data, int msgIndex){
        String key = DataBaseHelper.generateRandomKey();
        System.out.println("msgIndex=" + msgIndex);
        Comment newComment = new Comment(data, 0, key, msgs.get(msgIndex).getKey());
        msgs.get(msgIndex).addComment(newComment);
        DataBaseHelper.addComment(newComment);
        notifyDataSetChanged();
        return;
    }

    private void voteMsg(int msgIndex){
        Message msg = msgs.get(msgIndex);
        msg.increaseNumOfVote();
        Collections.sort(msgs);
        //QuickSortHelper.QuickSort(msgs, 0, msgs.size() - 1);
        DataBaseHelper.voteMessage(msg);
              //sort
        notifyDataSetChanged();
        return;
    }

    public void editMsgDialog(String text, final int msgIndex){
        final EditText editText = new EditText(context);
        editText.setText(text);
        new AlertDialog.Builder(context).setTitle("Edit")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(editText)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String data = editText.getText().toString();
                        System.out.println("data=" + data);
                        editMsg(data, msgIndex);
                    }
                }).setNegativeButton("Cancel", null).show();
        return;
    }

    private void addCommentDialog(final int msgIndex){
        final EditText editText = new EditText(context);
        editText.setText("");
        new AlertDialog.Builder(context).setTitle("Add Comment")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(editText)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String data = editText.getText().toString();
                        addComment(data, msgIndex);
                    }
                }).setNegativeButton("Cancel", null).show();
        return;
    }

    class EditMessageClickListener implements View.OnClickListener {
        private int msgIndex;
        public EditMessageClickListener(int index){
            msgIndex = index;
        }
        @Override
        public void onClick(View v) {
            editMsgDialog(msgs.get(msgIndex).getMsg(), msgIndex);
        }
    }

    class AddCommentClickListener implements View.OnClickListener {
        private int msgIndex;
        public AddCommentClickListener(int index){
            msgIndex = index;
        }
        @Override
        public void onClick(View v) {
            addCommentDialog(msgIndex);
        }
    }

    class VoteMessageClickListener implements View.OnClickListener {
        private int msgIndex;
        public VoteMessageClickListener(int index){
            msgIndex = index;
        }
        @Override
        public void onClick(View v) {
            voteMsg(msgIndex);
        }
    }
}
