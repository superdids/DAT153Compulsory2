package no.hib.dat153.compulsory2.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import no.hib.dat153.compulsory2.R;
import no.hib.dat153.compulsory2.persistence.ApplicationDatabase;
import no.hib.dat153.compulsory2.persistence.Person;
import no.hib.dat153.compulsory2.utils.Constants;

/**
 * Views an image and its corresponding name. This activity is used
 * by both NamesActivity and ImagesActivity to avoid redundancy.
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class ShowPersonActivity extends AppCompatActivity {

    /**
     * Key used to pass non-persistent data through an intent. This
     * key is used both my NamesActivity and ImagesActivity.
     */
    public static final String NAME = "no.hib.dat153.compulsory2.activities.NAME";

    /**
     * Database operations
     */
    private ApplicationDatabase myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_person);

        myDB = new ApplicationDatabase(this, null, null, 1);

        Bundle data = getIntent().getExtras();

        String name = data.getString(NAME);
      /*  SharedPreferences prefs = getSharedPreferences(
                Constants.PREFERENCES_FILE, Context.MODE_PRIVATE
        );*/
        

        /*String a,b;
        /*String a,b;
        if((a = prefs.getString(Constants.OWNER,null)) != null) {
            person = new Person(a, prefs.getString(a,null));
        } else*/
        Person person = myDB.find(name);
        assert person != null;
        InputStream stream;
        try {
            Uri uri = Uri.parse(person.getUriString());
            stream = getContentResolver().openInputStream(uri);
        } catch (Exception e) {
            throw new Error(e);
        }

        ImageView imageView = (ImageView) findViewById(R.id.imageOfPerson);
        imageView.setImageBitmap(BitmapFactory.decodeStream(stream));

        TextView textView = (TextView) findViewById(R.id.nameOfPerson);
        textView.setText(person.getName());
    }
}
