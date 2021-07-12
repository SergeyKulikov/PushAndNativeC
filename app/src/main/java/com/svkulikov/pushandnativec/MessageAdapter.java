package com.svkulikov.pushandnativec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<MessageData> messageList = new ArrayList<>();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    public MessageAdapter(Context context) {
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.message_item_layout, parent, false);
        // inflate(this.layoutId, parent, false);

        return new MessageViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        // CompanyProperty companyProperty = companyPropertyList.get(activeFolder).get(position);
        holder.tvMessage.setText(

                simpleDateFormat.format( messageList.get(position).getEventTime())+", "+
                messageList.get(position).getToHash()+", "+
                messageList.get(position).getHashValue());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void setList(List<MessageData> messages) {
        messageList.clear();
        messageList.addAll(messages);
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessage;

        @SuppressLint("CheckResult")
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }

}
