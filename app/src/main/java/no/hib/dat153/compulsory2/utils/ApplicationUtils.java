package no.hib.dat153.compulsory2.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import no.hib.dat153.compulsory2.persistence.Person;

public class ApplicationUtils {

    public static void rotate(ExifInterface exif, File file) throws Exception {
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int angle = 90;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90 :
                angle = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180 :
                angle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270 :
                angle = 270;
                break;
            default :
                break;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, null);
        Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotated.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte [] bitmapData = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapData);
        fos.flush();
        fos.close();
    }

    /**
     * Shuffles a list
     * @param list The list to be shuffled.
     */
    public static void shuffle(List<Person> list) {
        int n = list.size();
        Random random = new Random();
        random.nextInt();
        for(int i = 0; i < n; ++i) {
            int change = i + random.nextInt(n-i);
            swap(list, i, change);
        }
    }

    /**
     * Swaps the placement of two elements in a list.
     * @param list The list to perform operations on.
     * @param a The index of the first placement.
     * @param b The index of the other placement.
     */
    public static void swap(List<Person> list, int a, int b) {
        Person tmp = list.get(a);
        list.set(a, list.get(b));
        list.set(b, tmp);
    }

    /**
     * Retrieves an image's stream by an URI object.
     * @param context The context of the activity.
     * @param uri The URI object.
     * @return The InputStream of the URI object.
     */
    public static InputStream fetchImage(Context context, Uri uri) throws IOException {
        return context.getContentResolver().openInputStream(uri);
    }

    /**
     * Retrieves the URI of a drawable resource.
     * @param resources The available resources of the application.
     * @param id The id of the drawable resource.
     * @return Uri of a the drawable resource corresponding to the id.
     */
    public static Uri getResourceById(Resources resources, int id) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id));
    }
}