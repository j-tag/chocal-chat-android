package org.puresoftware.chocalandroid;

import android.graphics.Bitmap;

/**
 * org.puresoftware.chocalandroid
 * Created by Hesam Gholami on 2016/04/22 13:08.
 */
public class ImageMessage extends PlainMessage {

    protected Bitmap mPhoto;

    public ImageMessage() {
        this.mType = "image";
    }

    public ImageMessage(User user, String message, Bitmap photo) {
        super(user, message);
        this.mType = "image";
        this.mPhoto = photo;
    }

    @Override
    public Bitmap getPhoto() {
        return this.mPhoto;
    }

    public void setPhoto(Bitmap photo) {
        this.mPhoto = photo;
    }
}
