package com.example.cat.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.cat.R;
import com.example.cat.dataModels.ChatMessage;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<ChatMessage> {
    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<ChatMessage>  objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.listitem, parent, false);
        }
        TextView messageText = (TextView)convertView.findViewById(R.id.message_text);
        TextView messageUser = (TextView)convertView.findViewById(R.id.message_user);
        TextView messageTime = (TextView)convertView.findViewById(R.id.message_time);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.photoImageView);


        // Set their text
        ChatMessage model=getItem(position);

        boolean isPhoto = model.getPhotourl() != null;
        if (isPhoto) {
            Log.v("photo","photoloading");
            messageText.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Glide.with(imageView.getContext())
                    .load(model.getPhotourl())
                    .into(imageView);
        } else {
            messageText.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            messageText.setText(model.getMessageText());
        }
        messageUser.setText(model.getMessageUser());


        // Format the date before showing it
        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                model.getMessageTime()));
return convertView;
    }
}