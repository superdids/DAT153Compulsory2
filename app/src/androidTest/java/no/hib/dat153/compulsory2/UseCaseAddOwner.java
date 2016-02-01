package no.hib.dat153.compulsory2;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.EditText;

import no.hib.dat153.compulsory2.activities.AddOwnerActivity;

/**
 * Created by Didrik on 2/1/2016.
 */
public class UseCaseAddOwner extends ActivityInstrumentationTestCase2<AddOwnerActivity> {

    private AddOwnerActivity addOwnerActivity;

    public UseCaseAddOwner() {
        super(AddOwnerActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addOwnerActivity = getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SmallTest
    public void testAddOwner() {

        EditText editText = (EditText) addOwnerActivity.findViewById(R.id.ownerName);
        //editText.clearComposingText();
        TouchUtils.tapView(this, editText);
        //getInstrumentation().waitForIdleSync();
        sendKeys("My Name");
       // getInstrumentation().waitForIdleSync();

        assertEquals("My Name", editText.getText().toString());
    }
}
