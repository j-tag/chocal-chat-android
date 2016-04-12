package org.puresoftware.chocalandroid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by JTAG on 12/04/2016 17:39.
 */
public class ChocalImage {
    public static Bitmap getAvatarValidBitmap(Bitmap bitmap) {
        return cropToSquare(resize(bitmap, 128, 128));
    }
    public static Bitmap resize(Bitmap bitmap, int width, int height) {
        Bitmap croppedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        float originalWidth = bitmap.getWidth(), originalHeight = bitmap.getHeight();
        Canvas canvas = new Canvas(croppedBitmap);
        float scale = width / originalWidth;
        float xTranslation = 0.0f, yTranslation = (height - originalHeight * scale) / 2.0f;
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, transformation, paint);

        return croppedBitmap;
    }

    public static Bitmap cropToSquare(Bitmap bitmap){
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;

        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
    }
}
