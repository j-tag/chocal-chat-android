package org.puresoftware.chocalandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * org.puresoftware.chocalandroid
 * Created by Hesam Gholami on 2016/04/06 10:26.
 */
public class User {

    public User() {
        this.localId = counter++;
    }

    public User(String name, Bitmap avatar) {
        this.localId = counter++;
        this.name = name;
        this.avatar = avatar;
    }

    private static int counter = 0;

    int localId;
    String name;
    Bitmap avatar = null;

    public Drawable getAvatarDrawable(Context context) {
        if (avatar == null) {
            avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_avatar);
        }
        RoundedBitmapDrawable avatarRounded = RoundedBitmapDrawableFactory
                .create(context.getResources(), ChocalImage.getAvatarValidBitmap(avatar));
        avatarRounded.setCircular(true);
        return avatarRounded;
    }
}
