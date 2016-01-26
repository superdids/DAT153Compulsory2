package no.hib.dat153.compulsory2.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Database operations on the "persons" table, private to the application.
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class ApplicationDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "persons.db";
    private static final String TABLE_PERSONS = "persons";
    private static final String COLUMN_ID = "name";
    private static final String COLUMN_URI_STRING = "uriString";

    @SuppressWarnings("unused")
    public ApplicationDatabase(Context context, String name,
                               SQLiteDatabase.CursorFactory factory,
                               int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PERSONS + "(" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_URI_STRING + " TEXT " + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONS);
        onCreate(db);
    }

    public SQLiteDatabase getDB() {
        return getWritableDatabase();
    }

    /**
     * Determines whether an id exists in the database or not.
     * @param name The id being investigated.
     * @return True if the id exists, false otherwise.
     */
    public boolean exists(String name) {
        return find(name) != null;
    }

    /**
     * Searches the database for a specific id.
     * @param name The id being investigated.
     * @return A person object if the id exists, a null-reference otherwise.
     */
    public Person find(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PERSONS + " WHERE " +
                COLUMN_ID + "=\"" + name +"\";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Person ret = null;
        if (!cursor.isAfterLast()) {
            String n = cursor.getString(cursor.getColumnIndex("name"));
            String p = cursor.getString(cursor.getColumnIndex("uriString"));
            if(n != null && p != null)
                ret = new Person(n, p);
        }
        db.close();
        return ret;
    }

    /**
     * Adds a person to the database (given that his/her name doesn't exist).
     * @param person The person object begin persisted to the database.
     */
    public void addPerson(Person person) {
        if(exists(person.getName()))
            return;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, person.getName());
        values.put(COLUMN_URI_STRING, person.getUriString());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PERSONS, null, values);
        db.close();
    }

    /**
     * Removes a person from the database.
     * @param name The id of the person which shall be removed.
     */
    public void deletePerson(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERSONS + " WHERE " +
                COLUMN_ID + "=\"" + name + "\";");
        db.close();
    }

    /**
     * Removes all entries from the database.
     */
    public void clearDB() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERSONS + " WHERE 1;");
        db.close();
    }

    /**
     * Retrieves all entries from the database
     * @return A list of all person objects existing in the database.f
     */
    public ArrayList<Person> fetchAll() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Person> result = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_PERSONS + " WHERE 1";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String name, uriString;
        while(!cursor.isAfterLast()) {
            name = cursor.getString(cursor.getColumnIndex("name"));
            uriString = cursor.getString(cursor.getColumnIndex("uriString"));
            if(name != null && uriString != null)
                result.add(new Person(name, uriString));
            cursor.moveToNext();
        }
        db.close();
        return result;
    }
}