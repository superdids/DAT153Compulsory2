package no.hib.dat153.compulsory2.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import no.hib.dat153.compulsory2.R;
import no.hib.dat153.compulsory2.persistence.ApplicationDatabase;
import no.hib.dat153.compulsory2.persistence.Person;
import no.hib.dat153.compulsory2.utils.ApplicationUtils;
import no.hib.dat153.compulsory2.utils.Constants;

/**
 * The class to be considered as the entry point of the application.
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class MainActivity extends AppCompatActivity {

    private static final int TAKE_PHOTO = 0;
    private static final int PICK_IMAGE = 1;

    private static final int REQUEST_CAMERA_RW = 10;

    private static final String CAMERA = "Take Photo";
    private static final String GALLERY = "Choose from Gallery";
    private static final String CANCEL = "Cancel";


    /**
     * Options which will be presented in an AlertDialog
     */
    private String [] options = { CAMERA, GALLERY, CANCEL };

    /**
     * Gives information to a camera application where to
     * store a captured image.
     */
    private String pathToCapturedPhoto;

    /**
     * Database operations
     */
    private ApplicationDatabase myDB;

    /**
     * ID's referring the images in the drawable folder.
     */
    private int [] drawableIDs = { R.drawable.didrik, R.drawable.staale, R.drawable.viljar };

    /**
     * Names which shall be associated to the images.
     */
    private String [] names = { "Didrik", "Ståle", "Viljar" };

    private SharedPreferences owner;

    /**
     * Renders the view and adds drawable resources (if they don't exist in
     * the database).
     * @param savedInstanceState Information of this activity's previously frozen state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new ApplicationDatabase(this, null, null, 1);

        Resources resources = getResources();
        Uri uri;
        for(int x = 0; x < names.length; x++) {
            if(!myDB.exists(names[x])) {
                uri = ApplicationUtils.getResourceById(resources, drawableIDs[x]);
                myDB.addPerson(new Person(names[x], uri.toString()));
            }
        }

        owner = getSharedPreferences(
                Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);

       //SharedPreferences.Editor editor = owner.edit();
        //editor.clear(); editor.commit();

        if(owner.getString(Constants.OWNER, null) == null)
            startActivity(new Intent(MainActivity.this,
                    AddOwnerActivity.class));
        //if(owner.getAll().size() == 0)
         //   startActivity(new Intent(MainActivity.this,
           //       AddOwnerActivity.class));

    }

    /*@Override
    protected void onResume() {
        super.onResume();
        if(owner.getString(Constants.OWNER, null) == null)
            startActivity(new Intent(MainActivity.this, AddOwnerActivity.class));
    }*/

    /**
     * Pressing either the "Names", "Images" or "Learn" button, will start
     * either "NamesActivity", "ImagesActivity" or "LearnActivity",
     * respectively. The "Add Person" button will also invoke invoke
     * this method, but will use existing activities before resuming to
     * MainActivity.
     * @param view The item which has been clicked.
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.names :
                startActivity(new Intent(this, NamesActivity.class));
                break;
            case R.id.images :
                startActivity(new Intent(this, ImagesActivity.class));
                break;
            case R.id.learn :
                ArrayList<Person> list = myDB.fetchAll();
                if(list.size() == 0) {
                    String message = getResources().getString(R.string.toastEmptyLearn);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
                else
                    startActivity(new Intent(this, LearnActivity.class));
                break;
            case R.id.addPerson :
                addMember();
                break;
            default :
                break;
        }
    }

    /**
     * Adds a person to the database. If the input field contains a null-reference,
     * an empty string or is already contained in the database as a primary key,
     * the user will be given suitable information. The activity will otherwise
     * proceed with asking the user for an image which shall be associated to the
     * value entered in the input field.
     */
    private void addMember() {
        EditText editText = (EditText) findViewById(R.id.personName);
        String name = editText.getText().toString();
        if(name.equals("")) {
            String message = getResources().getString(R.string.toastEnterName);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } else if(myDB.exists(name)) {
            String messageA = getResources().getString(R.string.toastTheName);
            String messageB = getResources().getString(R.string.toastIsTaken);
            String message = messageA + name + messageB;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            showImageOptions();
        }
    }

    /**
     * Prompts options to the user, where he/she wants to capture an image
     * with a camera application, choose an image from the image gallery,
     * or simply cancel.
     */
    public void showImageOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Add a photo:");
        alertDialog.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which >= 0 && which < options.length) {
                    switch (options[which]) {
                        case CAMERA:
                            //Versions at or above API level 23 (Marshmallow) require runtime permissions
                            //Relevant permissions include CAMERA and WRITE_EXTERNAL.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                        && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    initiateCamera();
                                } else {
                                    //An explaining text will be given the user if necessary.
                                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                        String message = getResources().getString(R.string.toastCameraPermission);
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                    if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        String message = getResources().getString(R.string.toastWritePermission);
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }

                                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_RW);
                                }
                            } else {
                                initiateCamera();
                            }

                            break;
                        case GALLERY:
                            Intent galleryIntent;
                            //The application crashes with permission denial (MANAGE_DOCUMENTS)
                            //if the API level is at or above 19 (KitKat). No luck with declaring
                            //the permission in AndroidManifest.xml and requesting permission
                            if(Build.VERSION.SDK_INT < 19) {
                                galleryIntent = new Intent();
                                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                galleryIntent.setType("image/*");
                            } else {
                                galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                galleryIntent.setType("image/*");
                            }
                            startActivityForResult(galleryIntent, PICK_IMAGE);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        alertDialog.show();
    }

    /**
     * The device receives information of whether requested permissions have been
     * granted or not by the user.
     * @param requestCode Identifier in case the application has to request different
     *                    permissions at different places within a single activity.
     * @param permissions Permissions requested by the user
     * @param results Whether a permission has been granted or not by the user.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String [] permissions, @NonNull int [] results) {
        if(requestCode == REQUEST_CAMERA_RW) {
            if(results.length == 2
                    && results[0] == PackageManager.PERMISSION_GRANTED
                    && results[1] == PackageManager.PERMISSION_GRANTED) {
                initiateCamera();
            } else {
                String message = getResources().getString(R.string.permissionNotGranted);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, results);
        }
    }

    /**
     * The MainActivity will be resumed after an image capture / gallery pick.
     * @param requestCode Whether an image was captured by a camera application
     *                    or chosen from the image gallery.
     * @param resultCode Whether the result was successful or not.
     * @param data The data of the image (if chosen from gallery), passed as
     *             an intent.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK
                || (requestCode != TAKE_PHOTO && requestCode != PICK_IMAGE))
            return;
        Uri selectedImage;
        if(requestCode == TAKE_PHOTO) {
            File file = new File(pathToCapturedPhoto);
            try {
                ExifInterface exif = new ExifInterface(file.getPath());
                ApplicationUtils.rotate(exif, file);
            } catch(Exception e) { throw new Error(e); }

            selectedImage = Uri.fromFile(file);
            //Makes the image available in the gallery.
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, pathToCapturedPhoto);
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            assert data != null;
            selectedImage = data.getData();
        }

        EditText editText = (EditText) findViewById(R.id.personName);
        String name = editText.getText().toString();

        myDB.addPerson(new Person(name, selectedImage.toString()));
    }

    /**
     * Prepares the camera by creating a file and telling the
     * camera application where to save it.
     */
    private void initiateCamera() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            File file;
            try {
                file = createImageFile();
            } catch (Exception e) {
                throw new Error(e);
            }
            if (file != null) {
                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(file));
                startActivityForResult(photoIntent, TAKE_PHOTO);
            }
        }
    }

    /**
     * Creates a file at the external storage.
     * @return An empty JPEG file.
     * @throws IOException If the creation was unsuccessful.
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );
        File image = File.createTempFile(
                fileName,
                ".jpg",
                storageDirectory
        );
        pathToCapturedPhoto = image.getAbsolutePath();
        return image;
    }
}
