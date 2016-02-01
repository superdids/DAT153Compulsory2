package no.hib.dat153.compulsory2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import org.junit.Before;
import org.junit.Rule;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import no.hib.dat153.compulsory2.activities.AddOwnerActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.test.ViewAsserts.assertOnScreen;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

/**
 * Created by Didrik on 2/1/2016.
 */
public class UseCaseAddOwner extends ActivityInstrumentationTestCase2<AddOwnerActivity> {

    private AddOwnerActivity addOwnerActivity;

    @Rule
    public IntentsTestRule<AddOwnerActivity> intentsRule = new IntentsTestRule<>(AddOwnerActivity.class);

    public UseCaseAddOwner() {
        super(AddOwnerActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        addOwnerActivity = getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

   /* @SmallTest
    public void testAddOwner() {

        String input = "Hans";


        onView(withId(R.id.ownerName)).perform(typeText(input), closeSoftKeyboard());

        //Button button = (Button) addOwnerActivity.findViewById(R.id.ownerAdd);
        EditText editText = (EditText) addOwnerActivity.findViewById(R.id.ownerName);
        assertEquals(editText.getText().toString(), input);

        onView(withId(R.id.ownerAdd)).perform(click());

        String takePhoto = getString(R.string.galleryTakePhoto);


        Intent resultData = new Intent();
        File f = null;
        try {
            f = createImageFile();
        } catch(Exception e) { }
        resultData.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                11, resultData
        );

        intending(toPackage("com.android.camera2")).respondWith(result);

        onView(withText(takePhoto)).perform(click());

        intended(toPackage("com.android.camera2"));

        onData(allOf(withId(R.id.deleteNameButton)))
                .atPosition(3).perform(click());

        //TouchUtils.clickView(this, button);

        //editText.clearComposingText();
        //TouchUtils.tapView(this, editText);
        //getInstrumentation().waitForIdleSync();
        //sendKeys("My Name");
        // getInstrumentation().waitForIdleSync();

        // assertEquals("My Name", editText.getText().toString());
    }

    private static String getString(int id) {
        return InstrumentationRegistry.getTargetContext().getString(id);
    }*/



}
