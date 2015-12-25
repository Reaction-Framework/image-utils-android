package io.reactionframework.android.image.bitmapdecoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class BitmapDecoder {
    private final int mMaxWidth;
    private final int mMaxHeight;

    public BitmapDecoder(int maxWidth, int maxHeight) {
        mMaxWidth = maxWidth;
        mMaxHeight = maxHeight;
    }

    protected abstract Bitmap decode(BitmapFactory.Options options);

    public Bitmap decode() {
        // Decode bounds only
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decode(options);

        // Calculate scale
        int scale = 1;
        while (options.outWidth / scale > mMaxWidth ||
                options.outHeight / scale > mMaxHeight) {
            scale++;
        }

        scale--;
        if (scale < 1) {
            scale = 1;
        }

        // Decode full scaled bitmap
        options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = decode(options);
        if (bitmap == null) {
            return null;
        }

        // Resize bitmap to required max with or max height
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= mMaxWidth && height <= mMaxHeight) {
            return bitmap;
        }

        float newWidth = mMaxWidth;
        float newHeight = mMaxHeight;

        if ((float) width / mMaxWidth < (float) height / mMaxHeight) {
            newWidth = (newHeight / height) * width;
        } else {
            newHeight = (newWidth / width) * height;
        }

        Bitmap workBitmap = Bitmap.createScaledBitmap(bitmap, Math.round(newWidth), Math.round(newHeight), true);

        if (workBitmap != bitmap) {
            bitmap.recycle();
            bitmap = workBitmap;
        }

        return bitmap;
    }
}
