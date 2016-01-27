package no.hib.dat153.compulsory2.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import no.hib.dat153.compulsory2.R;
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

    /**
     * Retrieves the owner of the application
     * @param preferences A reference to the shared preferences file
     *                    to be read.
     * @return The owner as a Person-instance.
     */
    public static Person getOwner(SharedPreferences preferences) {
        String name = preferences.getString(Constants.OWNER, null);
        String uriString = preferences.getString(name, null);
        return new Person(name, uriString);
    }

    /**
     * Fills a layout with an image and name corresponding to the
     * values of a person object.
     * @param parent The parent layout.
     * @param context The context of the invoking activity.
     * @param person The values to be used within the parent layout.
     * @param identifications The identifiers to an ImageView and a
     *                        TextView.
     * @throws Exception If the person contains an invalid Uri value.
     */
    public static void generateOwnerView(FrameLayout parent, Context context, Person person,
                                         int [] identifications) throws Exception {
        ImageView imageView = (ImageView) parent.findViewById(identifications[0]);
        Uri uri = Uri.parse(person.getUriString());
        InputStream stream = context.getContentResolver().openInputStream(uri);

        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = (float) width / (float) 150;
        float scaleHeight = (float) height / (float) 150;
        float scale = scaleWidth < scaleHeight ? scaleHeight : scaleWidth;

        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / scale),
                (int) (height / scale), true);
        imageView.setImageBitmap(bitmap);

        TextView textView = (TextView) parent.findViewById(identifications[1]);
        textView.setText(person.getName());
    }
}