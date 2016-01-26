package no.hib.dat153.compulsory2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import no.hib.dat153.compulsory2.R;
import no.hib.dat153.compulsory2.persistence.ApplicationDatabase;
import no.hib.dat153.compulsory2.persistence.Person;

/**
 * A custom ArrayAdapter implementation, to render names in a ListView,
 * as well as a corresponding button to delete selected entry.
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class NameAdapter extends ArrayAdapter<Person> {

    /**
     * The list which the ListView shall be associated with.
     */
    private ArrayList<Person> personList;

    /**
     * The context of the activity initializing an instance of this class.
     */
    private Context context;

    /**
     * Database operations to be used when the delete button is pressed.
     */
    private ApplicationDatabase myDB;

    /**
     * Constructor assigning values to the field varaibles and invoking the
     * constructor superclass.
     * @param context The context of the activity initializing an instance of this class.
     * @param id The id of the ListView.
     * @param personList The list which the ListView shall be associated with.
     */
    public NameAdapter(Context context, int id, ArrayList<Person> personList,
                       ApplicationDatabase myDB) {
        super(context, id, personList);
        this.context = context;
        this.personList = personList;
        this.myDB = myDB;
    }

    /**
     * Renders a name with a corresponding delete-button as
     * an entry in the ListView.
     * @param position The index of the item/person
     * @param view The item to be rendered
     * @param parent The parent of the item, being the ListView.
     * @return The item (modified) to be rendered.
     */
    public View getView(final int position, View view, ViewGroup parent) {
        View v = view;
        if(v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.list_view_names, null);
        }
        Person person = personList.get(position);
        if(person != null) {
            TextView textView = (TextView) v.findViewById(R.id.listViewNames);
            textView.setText(person.getName());

            Button button = (Button) v.findViewById(R.id.deleteNameButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDB.deletePerson(personList.get(position).getName());
                    personList.remove(position);
                    NameAdapter.this.notifyDataSetChanged();
                }
            });
        }
        return v;
    }
}