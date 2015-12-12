package io.reactionframework.android.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils {
    private static final String LOG_TAG = ImageUtils.class.getSimpleName();

    public static Bitmap rotateBitmap(Bitmap original, int rotation)
    {
        if (rotation != 0) {
            Bitmap oldBitmap = original;

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            original = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);

            oldBitmap.recycle();
        }

        return original;
    }

    public static Uri storePhoto(Context context, Bitmap bitmap) {
        return storePhoto(context, bitmap, String.format("IMG_%s", new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date())));
    }

    public static Uri storePhoto(Context context, Bitmap bitmap, String photoName) {
        File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        File mediaFile = new File(String.format("%s%s%s.jpg", mediaStorageDir.getPath(), File.separator, photoName));

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            FileOutputStream stream = new FileOutputStream(mediaFile);
            stream.write(out.toByteArray());
            stream.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error storing photo to disk.", e);
            e.printStackTrace();
            return null;
        }

        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri = Uri.fromFile(mediaFile);
        mediaScannerIntent.setData(fileContentUri);
        context.sendBroadcast(mediaScannerIntent);

        return fileContentUri;
    }

    public static String bitmapToString(Bitmap bitmap) {
        return bitmapToString(bitmap, Bitmap.CompressFormat.PNG);
    }

    public static String bitmapToString(Bitmap bitmap, Bitmap.CompressFormat compressFormat) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, 100, out);
        return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap bitmapFromString(String encoded) {
        byte[] decodedBytes = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
