package org.puresoftware.chocalandroid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        ViewHolder viewHolder;

        if (convertView == null) {
            rowView = LayoutInflater.from(mContext).inflate(R.layout.list_item_bubble, parent,
                    false);

            viewHolder = new ViewHolder();
            viewHolder.mContentLayout = (LinearLayout) rowView.findViewById(R.id.layout_chat_bubble_content);
            viewHolder.mNameView = (TextView) rowView.findViewById(R.id.txt_bubble_name);
            viewHolder.mAvatarView = (ImageView) rowView.findViewById(R.id.img_avatar);
            viewHolder.mMessageView = (TextView) rowView.findViewById(R.id.txt_bubble_message);
            viewHolder.mPhotoView = (ImageView) rowView.findViewById(R.id.img_bubble_photo);

            rowView.setTag(viewHolder);
        } else {
            rowView = convertView;
            viewHolder = (ViewHolder) rowView.getTag();
        }

        // Handle info message
        if (message.getType().equalsIgnoreCase("info")) {
            return getInfoView(message.getMessage(), viewHolder, rowView);
        }

        // Check whether to see current message is a self message or not
        if (message.isSelfMessage()) {
            // Self message
            viewHolder.mContentLayout.setBackgroundResource(R.drawable.chat_bubble_self);
            viewHolder.mNameView.setText(R.string.you);
            viewHolder.mAvatarView.setVisibility(View.GONE);
        } else {
            viewHolder.mContentLayout.setBackgroundResource(R.drawable.chat_bubble);
            viewHolder.mNameView.setText(message.getUser().name);
            viewHolder.mAvatarView.setVisibility(View.VISIBLE);
            viewHolder.mAvatarView.setImageDrawable(message.getUser().getAvatarDrawable(mContext));
        }

        viewHolder.mNameView.setVisibility(View.VISIBLE);
        viewHolder.mMessageView.setVisibility(View.VISIBLE);
        viewHolder.mMessageView.setText(message.getMessage());
        viewHolder.mPhotoView.setImageBitmap(message.getPhoto());
        viewHolder.mPhotoView.setVisibility(View.VISIBLE);

        return rowView;
    }

    protected View getInfoView(String message, ViewHolder viewHolder, View rowView) {
        viewHolder.mContentLayout.setBackgroundResource(R.drawable.chat_bubble_info);
        viewHolder.mNameView.setVisibility(View.GONE);
        viewHolder.mAvatarView.setVisibility(View.GONE);
        viewHolder.mPhotoView.setVisibility(View.GONE);
        viewHolder.mMessageView.setText(message);

        return rowView;
    }

    class ViewHolder {
        LinearLayout mContentLayout;

        TextView mNameView;
        ImageView mAvatarView;
        TextView mMessageView;
        ImageView mPhotoView;
    }
}
