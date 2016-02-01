package no.hib.dat153.compulsory2.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import no.hib.dat153.compulsory2.R;
import no.hib.dat153.compulsory2.persistence.ApplicationDatabase;
import no.hib.dat153.compulsory2.persistence.Person;
import no.hib.dat153.compulsory2.utils.ApplicationUtils;
import no.hib.dat153.compulsory2.utils.Constants;
import no.hib.dat153.compulsory2.utils.Validator;

public class AddOwnerActivity extends AppCompatActivity {

    private ApplicationDatabase myDB;

    private EditText editText;
    private TextView errorMessage;
    private Button submit;
    private ImageView image;

    private String invalid, missingImage, pathToCapturedPhoto, resumedToScreen;
    private Uri selectedImage;

    private static final int REQUEST_CAMERA_RW = 2;
    private static final int PICK_IMAGE = 10;
    private static final int TAKE_PHOTO = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_owner);

        myDB = new ApplicationDatabase(this, null, null, 1);

        editText = (EditText) findViewById(R.id.ownerName);
        editText.addTextChangedListener(textWatcher);

        //TODO implement language support for norwegian+spanish.
        invalid = "The name is invalid";
        missingImage = "Missing an image, please add";
        resumedToScreen = "You need to register before you can do anything else.";

        errorMessage = (TextView) findViewById(R.id.ownerError);
        errorMessage.setText(invalid);
        errorMessage.setVisibility(View.VISIBLE);

        submit = (Button) findViewById(R.id.ownerSubmit);
        image = (ImageView) findViewById(R.id.ownerImage);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (intent.hasExtra(Constants.MESSAGE_OWNER_NOT_ADDED)
                && extras.getBoolean(Constants.MESSAGE_OWNER_NOT_ADDED, false)) {
            Toast.makeText(getApplicationContext(), resumedToScreen, Toast.LENGTH_LONG).show();
        }
    }

    public void onSubmitClick(View view) {
        String result = editText.getText().toString();

        String uriString = selectedImage.toString();

        SharedPreferences settings = getSharedPreferences(
                Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor manage = settings.edit();

        //make sure the SharedPreferences file is empty.
        manage.clear();
        manage.putString(Constants.OWNER, result);
        manage.putString(result, uriString);
        manage.commit();
        Intent intent = new Intent(AddOwnerActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }



    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!Validator.validName(s.toString())) {
                errorMessage.setText(invalid);
                showErrors();
            } else {
                if (!hasImage()) {
                    errorMessage.setText(missingImage);
                    showErrors();
                } else {
                    hideErrors();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //do nothing
        }
    };


    /**
     * Checks the validity of the input field.
     *
     * @return False if the input field is empty, true otherwise.
     */
    public boolean valid() {
        return ! "".equals(editText.getText().toString());
    }

    private void showErrors() {
        errorMessage.setVisibility(View.VISIBLE);
        submit.setVisibility(View.INVISIBLE);
    }

    private void hideErrors() {
        errorMessage.setVisibility(View.INVISIBLE);
        submit.setVisibility(View.VISIBLE);
    }

    private boolean hasImage() {
        Drawable drawable = image.getDrawable();
        if (drawable != null && (drawable instanceof BitmapDrawable))
            return ((BitmapDrawable) drawable).getBitmap() != null;
        return false;
    }

    public void onAddPhotoClick(View view) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddOwnerActivity.this);
        alertDialog.setTitle(getString(R.string.galleryAddPhoto));
        alertDialog.setItems(Constants.OPTIONS, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which >= 0 && which < Constants.OPTIONS.length) {
                    switch (Constants.OPTIONS[which]) {
                        case Constants.CAMERA:
                            //Versions at or above API level 23 (Marshmallow) require runtime permissions
                            //Relevant permissions include CAMERA and WRITE_EXTERNAL.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                        && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    initiateCamera();
                                } else {
                                    //An explaining text will be given the user if necessary.
                                    //TODO add message to print
                                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                        //String message = getResources().getString(R.string.toastCameraPermission);
                                        //  Toast.makeText(AddOwnerActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                    //TODO add message to print
                                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        //String message = getResources().getString(R.string.toastWritePermission);
                                        //Toast.makeText(AddOwnerActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }

                                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_RW);
                                }
                            } else {
                                initiateCamera();
                            }

                            break;
                        case Constants.GALLERY:
                            Intent galleryIntent;
                            //The application crashes with permission denial (MANAGE_DOCUMENTS)
                            //if the API level is at or above 19 (KitKat). No luck with declaring
                            //the permission in AndroidManifest.xml and requesting runtime-permission
                            if (Build.VERSION.SDK_INT < 19) {
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
     *
     * @param requestCode Identifier in case the application has to request different
     *                    permissions at different places within a single activity.
     * @param permissions Permissions requested by the user
     * @param results     Whether a permission has been granted or not by the user.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        if (requestCode == REQUEST_CAMERA_RW) {
            if (results.length == 2
                    && results[0] == PackageManager.PERMISSION_GRANTED
                    && results[1] == PackageManager.PERMISSION_GRANTED) {
                initiateCamera();
            } else {
                //TODO add message if permissions was not granted.
                //String message = getResources().getString(R.string.permissionNotGranted);
                //Toast.makeText(AddOwnerActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, results);
        }
    }

    /**
     * The MainActivity will be resumed after an image capture / gallery pick.
     *
     * @param requestCode Whether an image was captured by a camera application
     *                    or chosen from the image gallery.
     * @param resultCode  Whether the result was successful or not.
     * @param data        The data of the image (if chosen from gallery), passed as
     *                    an intent.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK
                || (requestCode != TAKE_PHOTO && requestCode != PICK_IMAGE))
            return;
        if (requestCode == TAKE_PHOTO) {
            File file = new File(pathToCapturedPhoto);
            try {
                ExifInterface exif = new ExifInterface(file.getPath());
                ApplicationUtils.rotate(exif, file);
            } catch (Exception e) {
                throw new Error(e);
            }

            selectedImage = Uri.fromFile(file);
            //Makes the image available in the gallery.
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, pathToCapturedPhoto);
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            selectedImage = data.getData();
        }
        try {
            InputStream stream = getContentResolver().openInputStream(selectedImage);
            image.setImageBitmap(BitmapFactory.decodeStream(stream));
        } catch (IOException e) {
            throw new Error(e);
        }

        if (valid()) {
            errorMessage.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Prepares the camera by creating a file and telling the
     * camera application where to write the captured image.
     */
    private void initiateCamera() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            if (hasImage()) {
                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        selectedImage);
                startActivityForResult(photoIntent, TAKE_PHOTO);
            } else {
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
    }

    /**
     * Creates a file at the external storage.
     *
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

    /**
     * Deletes a file if the user already exists. Drawable resources
     * will remain on the device. The method won't be used, as we will
     * overwrite a new picture to the file instead.
     */
    public void removeFile() {
        String[] partsOfUri = selectedImage.toString().split("/");
        int resourceId = getResources().getIdentifier(partsOfUri[partsOfUri.length - 1], "drawable", getPackageName());
        if (resourceId <= 0) {
            File file = new File(selectedImage.getPath());
            if (file.exists() && file.delete()) {
                //After deleting the image, the gallery has to be informed of the removal.
                //ACTION_MEDIA_MOUNTED is deprecated in API level 14 and above, so there will be
                //different behaviour based on the API level.
                if (Build.VERSION.SDK_INT < 14) {
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                } else {
                    MediaScannerConnection.scanFile(this, new String[]{
                            Environment.getExternalStorageDirectory().toString()
                    }, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
                }
            }
        }
    }
}
