package no.hib.dat153.compulsory2;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;

import org.junit.runner.RunWith;

import no.hib.dat153.compulsory2.activities.AddOwnerActivity;
import no.hib.dat153.compulsory2.activities.MainActivity;



public class UseCaseAddPerson extends ActivityInstrumentationTestCase2<MainActivity> {

    public UseCaseAddPerson() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testView() {
        MainActivity mainActivity = getActivity();
        EditText editText = (EditText) mainActivity.findViewById(R.id.personName);
        assertFalse(editText != null);
        //sendKeys("123");
       // Button button = (Button) mainActivity.findViewById(R.id.addPerson);
        //TouchUtils.tapView(this, button);
    }
}
