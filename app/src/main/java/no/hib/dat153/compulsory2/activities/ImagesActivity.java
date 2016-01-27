package no.hib.dat153.compulsory2.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;

import no.hib.dat153.compulsory2.R;
import no.hib.dat153.compulsory2.adapters.ImageAdapter;
import no.hib.dat153.compulsory2.persistence.ApplicationDatabase;
import no.hib.dat153.compulsory2.persistence.Person;
import no.hib.dat153.compulsory2.utils.ApplicationUtils;
import no.hib.dat153.compulsory2.utils.Constants;

/**
 * Views clickable items in a ListView. Each item contains an image which is
 * associated to a name (the name is not shown beside the image).
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class ImagesActivity extends AppCompatActivity {


    /**
     * Custom adapter extending ArrayAdapter.
     */
    private ImageAdapter imageAdapter;

    /**
     * Database operations
     */
    private ApplicationDatabase myDB;

    /**
     * The list which shall be associated to the adapter.
     */
    private ArrayList<Person> list;


    /**
     * Renders an AdapterView after associating the adapter to the list.
     * @param savedInstanceState Information of this activity's previously frozen state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);



        myDB = new ApplicationDatabase(this, null, null, 1);
        list = myDB.fetchAll();

        SharedPreferences preferences = getSharedPreferences(
                Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);

        Person owner = ApplicationUtils.getOwner(preferences);
        if(owner.isNotEmpty()) {
            try {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.imageFrameLayout);
                ApplicationUtils.generateOwnerView(frameLayout, getApplicationContext(), owner,
                        new int [] { R.id.imageOwnerImagePicture, R.id.imageOwnerImageText });
            } catch(Exception e) { throw new Error(e); }
        }

        ListView listView = (ListView) findViewById(R.id.imageListView);
        imageAdapter = new ImageAdapter(getApplicationContext(), R.layout.list_view_images,
                list, myDB);


        listView.setAdapter(imageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(ImagesActivity.this, ShowPersonActivity.class);
                intent.putExtra(ShowPersonActivity.NAME, list.get(position).getName());
                startActivity(intent);
            }
        });

    }



    /**
     * Navigates the user back to the main screen.
     * @param view The button invoking this method.
     */
    public void backToMainScreen(View view) {
        Intent intent = new Intent(ImagesActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
