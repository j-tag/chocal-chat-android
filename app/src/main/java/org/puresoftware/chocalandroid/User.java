package org.puresoftware.chocalandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * Created by JTAG on 06/04/2016 10:26.
 */
public class User {

    public User() {
    }

    public User(String name, Bitmap avatar) {
        this.id = counter++;
        this.name = name;
        this.avatar = avatar;
    }

    private static int counter = 1;

    int id;
    String name;
    Bitmap avatar = null;

    public Drawable getAvatarDrawable(Context context) {
        if (avatar == null) {
            avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_avatar);
        }
        RoundedBitmapDrawable avatarRounded = RoundedBitmapDrawableFactory.create(context.getResources(), avatar);
        avatarRounded.setCircular(true);
        return avatarRounded;
    }
}
