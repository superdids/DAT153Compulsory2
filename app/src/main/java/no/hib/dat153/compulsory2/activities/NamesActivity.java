package no.hib.dat153.compulsory2.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;

import no.hib.dat153.compulsory2.R;
import no.hib.dat153.compulsory2.adapters.NameAdapter;
import no.hib.dat153.compulsory2.persistence.ApplicationDatabase;
import no.hib.dat153.compulsory2.persistence.Person;
import no.hib.dat153.compulsory2.utils.ApplicationUtils;
import no.hib.dat153.compulsory2.utils.Constants;

/**
 * Views clickable items in a ListView. Each item contains a name which is
 * associated to an image (the image is not shown beside the name).
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class NamesActivity extends AppCompatActivity {

    /**
     * Database operations
     */
    private ApplicationDatabase myDB;

    /**
     * The list which shall be associated to the adapter
     */
    private ArrayList<Person> list;

    /**
     * Custom adapter extending ArrayAdapter
     */
    private NameAdapter nameAdapter;

    /**
     * Renders an AdapterView after associating the adapter to the list.
     *
     * @param savedInstanceState Information of this activity's previously frozen state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);

        myDB = new ApplicationDatabase(this, null, null, 1);
        list = myDB.fetchAll();

        SharedPreferences preferences = getSharedPreferences(
                Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);

        Person owner = ApplicationUtils.getOwner(preferences);
        if(owner.isNotEmpty()) {
            try {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.nameFrameLayout);
                ApplicationUtils.generateOwnerView(frameLayout, getApplicationContext(), owner,
                        new int [] { R.id.nameOwnerImagePicture, R.id.nameOwnerImageText} );
            } catch(Exception e) { throw new Error(e); }
        }

        ListView listView = (ListView) findViewById(R.id.nameListView);
        nameAdapter = new NameAdapter(this, R.layout.list_view_names, list, myDB);

        listView.setAdapter(nameAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(NamesActivity.this, ShowPersonActivity.class);
                intent.putExtra(ShowPersonActivity.NAME, list.get(position).getName());
                startActivity(intent);
            }
        });
    }

    public void backToMainScreen(View view) {
        Intent intent = new Intent(NamesActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
