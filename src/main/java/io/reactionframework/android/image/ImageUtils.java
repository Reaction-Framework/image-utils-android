package io.reactionframework.android.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import io.reactionframework.android.image.bitmapdecoder.BitmapFromArrayDecoder;
import io.reactionframework.android.image.bitmapdecoder.BitmapFromFileUrlDecoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils {
    private static final String LOG_TAG = ImageUtils.class.getSimpleName();

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }

    public static Bitmap bitmapFromByteArray(byte[] array, int maxWidth, int maxHeight) {
        return new BitmapFromArrayDecoder(array, maxWidth, maxHeight).decode();
    }

    public static Bitmap bitmapFromFileUrl(String fileUrl, int maxWidth, int maxHeight) {
        return new BitmapFromFileUrlDecoder(fileUrl, maxWidth, maxHeight).decode();
    }

    public static String dataToBase64String(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static byte[] dataFromBase64String(String encoded) {
        return Base64.decode(encoded, Base64.DEFAULT);
    }

    public static Uri storeInCameraRoll(Context context, byte[] data) {
        return storeInCameraRoll(context, data, null);
    }

    public static Uri storeInCameraRoll(Context context, byte[] data, String photoName) {
        return storeOnDisc(context, data, photoName, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
    }

    public static Uri storeInPictures(Context context, byte[] data) {
        return storeInPictures(context, data, null);
    }

    public static Uri storeInPictures(Context context, byte[] data, String photoName) {
        return storeOnDisc(context, data, photoName, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
    }

    private static Uri storeOnDisc(Context context, byte[] data, String photoName, File directory) {
        if (TextUtils.isEmpty(photoName)) {
            photoName = String.format("IMG_%s", new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()));
        }

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return null;
            }
        }

        File imageFile = new File(String.format("%s%s%s.jpg", directory.getPath(), File.separator, photoName));

        try {
            FileOutputStream stream = new FileOutputStream(imageFile);
            stream.write(data);
            stream.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error storing photo to disk.", e);
            e.printStackTrace();
            return null;
        }

        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri = Uri.fromFile(imageFile);
        mediaScannerIntent.setData(fileContentUri);
        context.sendBroadcast(mediaScannerIntent);

        return fileContentUri;
    }
}
