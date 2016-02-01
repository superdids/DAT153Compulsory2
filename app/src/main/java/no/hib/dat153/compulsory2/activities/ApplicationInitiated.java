package no.hib.dat153.compulsory2.activities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import no.hib.dat153.compulsory2.persistence.ApplicationDatabase;
import no.hib.dat153.compulsory2.utils.Constants;


/**
 * Overriding the Application class. We are having issues using
 * the URI from shared preferences after device reboot.
 *
 * @author Didrik Emil Aubert
 * @author Viljar Buen Rolfsen
 * @author Ståle André Mikalsen
 */
public class ApplicationInitiated extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences owner = getSharedPreferences(
                Constants.PREFERENCES_FILE, Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = owner.edit();
        editor.clear();
        editor.commit();
        ApplicationDatabase myDB = new ApplicationDatabase(this, null, null, 1);
        myDB.clearDB();
    }
}
