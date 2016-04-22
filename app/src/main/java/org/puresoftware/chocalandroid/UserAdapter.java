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
 * Created by Hesam Gholami on 2016/04/06 10:15.
 */
public class UserAdapter extends ArrayAdapter {

    private Context mContext;
    private ArrayList<User> mUsers;

    public UserAdapter(AppCompatActivity activity) {
        super(activity, R.layout.list_item_user);
        mContext = activity;
        mUsers = Chocal.getUsers();
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mUsers.get(position).localId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        User user = mUsers.get(position);
        TextView nameView;
        ImageView avatarView;

        if (convertView == null) {
            rowView = LayoutInflater.from(mContext).inflate(R.layout.list_item_user, parent, false);

            nameView =(TextView) rowView.findViewById(R.id.user_name);
            avatarView =(ImageView) rowView.findViewById(R.id.user_avatar);

            rowView.setTag(R.integer.user_list_name_tag, nameView);
            rowView.setTag(R.integer.user_list_avatar_tag, avatarView);
        } else {
            rowView = convertView;
            nameView =(TextView) rowView.getTag(R.integer.user_list_name_tag);
            avatarView =(ImageView) rowView.getTag(R.integer.user_list_avatar_tag);
        }

        nameView.setText(user.name);
        avatarView.setImageDrawable(user.getAvatarDrawable(mContext));
        return rowView;
    }

}
