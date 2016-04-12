package org.puresoftware.chocalandroid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * org.puresoftware.chocalandroid
 * Created by Hesam Gholami on 2016/04/06 10:15.
 */
public class UserAdapter extends BaseAdapter {

    private Context mContext;
    private List<User> mUsers;

    public UserAdapter(AppCompatActivity activity) {
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
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView nameView;
        ImageView avatarView;

        rowView = inflater.inflate(R.layout.list_item_user, parent, false);
        nameView =(TextView) rowView.findViewById(R.id.user_name);
        avatarView =(ImageView) rowView.findViewById(R.id.user_avatar);
        nameView.setText(user.name);
        avatarView.setImageDrawable(user.getAvatarDrawable(mContext));
        return rowView;
    }

}
