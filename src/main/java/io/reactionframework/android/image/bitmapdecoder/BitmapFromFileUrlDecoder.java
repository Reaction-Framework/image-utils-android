package io.reactionframework.android.image.bitmapdecoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapFromFileUrlDecoder extends BitmapDecoder {
    private String mFileUrl;

    public BitmapFromFileUrlDecoder(String fileUrl, int maxWidth, int maxHeight) {
        super(maxWidth, maxHeight);
        mFileUrl = fileUrl;
    }

    @Override
    protected Bitmap decode(BitmapFactory.Options options) {
        return BitmapFactory.decodeFile(mFileUrl, options);
    }
}
