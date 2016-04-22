package org.puresoftware.chocalandroid;

import android.graphics.Bitmap;

/**
 * org.puresoftware.chocalandroid
 * Created by Hesam Gholami on 2016/04/22 13:04.
 */
public class PlainMessage implements IMessage {

    private static int counter = 0;

    protected int mLocalId;
    protected String mType;
    protected User mUser;
    protected String mMessage;
    protected boolean mBSelfMessage = false;

    public PlainMessage() {
        mLocalId = counter++;
        this.mType = "plain";
    }

    public PlainMessage(User user, String message) {
        mLocalId = counter++;
        this.mType = "plain";
        this.mUser = user;
        this.mMessage = message;
    }

    @Override
    public int getLocalId() {
        return mLocalId;
    }

    @Override
    public String getType() {
        return this.mType;
    }

    @Override
    public User getUser() {
        return this.mUser;
    }

    @Override
    public String getMessage() {
        return this.mMessage;
    }

    @Override
    public Bitmap getPhoto() {
        return null;
    }

    public boolean isSelfMessage() {
        return this.mBSelfMessage;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public void setPhoto(Bitmap photo) {}

    public void setIsSelfMessage(boolean isSelfMessage) {
        this.mBSelfMessage = isSelfMessage;
    }

    public static void resetCounter() {
        counter = 0;
    }
}
