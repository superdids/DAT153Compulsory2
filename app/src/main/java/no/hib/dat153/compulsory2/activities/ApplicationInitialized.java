package no.hib.dat153.compulsory2.activities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import no.hib.dat153.compulsory2.persistence.ApplicationDatabase;
import no.hib.dat153.compulsory2.utils.Constants;

/**
 * Created by stamik on 01.02.2016.
 */
public class ApplicationInitialized extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences owner = getSharedPreferences(
                Constants.PREFERENCES_FILE, Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = owner.edit();
        editor.clear(); editor.commit();

        ApplicationDatabase myDB = new ApplicationDatabase(this, null, null, 1);
        myDB.clearDB();
    }
}
