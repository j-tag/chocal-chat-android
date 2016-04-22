package org.puresoftware.chocalandroid;

import android.graphics.Bitmap;

/**
 * org.puresoftware.chocalandroid
 * Created by Hesam Gholami on 2016/04/22 13:02.
 */
public interface IMessage {
    int getLocalId();
    String getType();
    User getUser();
    String getMessage();
    Bitmap getPhoto();

    void setType(String type);
    void setUser(User user);
    void setMessage(String message);
    void setPhoto(Bitmap photo);
}
