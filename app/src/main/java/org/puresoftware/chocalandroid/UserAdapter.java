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
        ViewHolder viewHolder;
        User user = mUsers.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            rowView = LayoutInflater.from(mContext).inflate(R.layout.list_item_user, parent, false);

            viewHolder.mNameView =(TextView) rowView.findViewById(R.id.user_name);
            viewHolder.mAvatarView =(ImageView) rowView.findViewById(R.id.user_avatar);

            rowView.setTag(viewHolder);
        } else {
            rowView = convertView;
            viewHolder =(ViewHolder) rowView.getTag();
        }

        viewHolder.mNameView.setText(user.name);
        viewHolder.mAvatarView.setImageDrawable(user.getAvatarDrawable(mContext));
        return rowView;
    }

    class ViewHolder {
        TextView mNameView;
        ImageView mAvatarView;
    }

}
