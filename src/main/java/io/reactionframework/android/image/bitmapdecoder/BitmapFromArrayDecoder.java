package io.reactionframework.android.image.bitmapdecoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapFromArrayDecoder extends BitmapDecoder {
    private final byte[] mArray;

    public BitmapFromArrayDecoder(byte[] array, int maxWidth, int maxHeight) {
        super(maxWidth, maxHeight);
        mArray = array;
    }

    @Override
    protected Bitmap decode(BitmapFactory.Options options) {
        return BitmapFactory.decodeByteArray(mArray, 0, mArray.length, options);
    }
}
