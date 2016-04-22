package org.puresoftware.chocalandroid;

/**
 * org.puresoftware.chocalandroid
 * Created by Hesam Gholami on 2016/04/23 01:34.
 */
public class InfoMessage extends PlainMessage {
    public InfoMessage() {
        this.mType = "info";
    }

    public InfoMessage(String message) {
        super(Chocal.getCurrentUser(), message);
        this.mType = "info";
    }
}
