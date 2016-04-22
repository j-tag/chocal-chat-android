package org.puresoftware.chocalandroid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * org.puresoftware.chocalandroid
 * Created by Hesam Gholami on 2016/04/22 12:58.
 */
public class ChatAdapter extends ArrayAdapter {
    private Context mContext;
    private ArrayList<IMessage> mMessages;

    public ChatAdapter(AppCompatActivity activity) {
        super(activity, R.layout.list_item_bubble);
        mContext = activity;
        mMessages = Chocal.getMessages();
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMessages.get(position).getLocalId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        IMessage message = mMessages.get(position);
        TextView nameView;
        TextView messageView;
        ImageView avatarView;
        ImageView photoView;

        if (convertView == null) {
            rowView = LayoutInflater.from(mContext).inflate(R.layout.list_item_bubble, parent, false);

            nameView =(TextView) rowView.findViewById(R.id.txt_bubble_name);
            messageView =(TextView) rowView.findViewById(R.id.txt_bubble_message);
            avatarView =(ImageView) rowView.findViewById(R.id.img_avatar);
            photoView =(ImageView) rowView.findViewById(R.id.img_bubble_photo);

            rowView.setTag(R.integer.message_list_name_tag, nameView);
            rowView.setTag(R.integer.message_list_avatar_tag, avatarView);
            rowView.setTag(R.integer.message_list_message_tag, messageView);
            rowView.setTag(R.integer.message_list_photo_tag, photoView);
        } else {
            rowView = convertView;
            nameView =(TextView) rowView.getTag(R.integer.message_list_name_tag);
            avatarView =(ImageView) rowView.getTag(R.integer.message_list_avatar_tag);
            messageView =(TextView) rowView.getTag(R.integer.message_list_message_tag);
            photoView =(ImageView) rowView.getTag(R.integer.message_list_photo_tag);
        }

        nameView.setText(message.getUser().name);
        avatarView.setImageDrawable(message.getUser().getAvatarDrawable(mContext));
        messageView.setText(message.getMessage());
        photoView.setImageBitmap(message.getPhoto());
        return rowView;
    }
}
