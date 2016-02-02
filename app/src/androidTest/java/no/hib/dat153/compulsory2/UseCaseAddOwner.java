package no.hib.dat153.compulsory2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
<<<<<<< HEAD

import org.hamcrest.Matcher;
=======
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
>>>>>>> b0b6ac2d2d5277d618dde87fccd1557014ee018c
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
<<<<<<< HEAD

import no.hib.dat153.compulsory2.activities.MainActivity;

=======

import no.hib.dat153.compulsory2.activities.AddOwnerActivity;
import no.hib.dat153.compulsory2.activities.MainActivity;

import static android.support.test.espresso.Espresso.onData;
>>>>>>> b0b6ac2d2d5277d618dde87fccd1557014ee018c
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
<<<<<<< HEAD
=======
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
>>>>>>> b0b6ac2d2d5277d618dde87fccd1557014ee018c
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
<<<<<<< HEAD
=======
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
>>>>>>> b0b6ac2d2d5277d618dde87fccd1557014ee018c
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
<<<<<<< HEAD
import static org.hamcrest.Matchers.allOf;


/**
 * Testing the add owner use-case.
 * @author Didrik Emil Aubert
 * @author Viljar Buen Rolfsen
 * @author Ståle André Mikalsen
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UseCaseAddOwner {

    @Rule
    public ActivityTestRule mainActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, true);
    private Instrumentation.ActivityResult activityResult;
    private String name = "Snoopey";

    private static String getString(int id) {
        return InstrumentationRegistry.getTargetContext().getString(id);
    }

    @Before
    public void setupImageUri() {
        Resources resources = InstrumentationRegistry.getTargetContext().getResources();
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources
                .getResourcePackageName(
                        R.mipmap.ic_launcher) + '/' + resources.getResourceTypeName(
                R.mipmap.ic_launcher) + '/' + resources.getResourceEntryName(
                R.mipmap.ic_launcher));
        Intent resultData = new Intent();
        resultData.setData(imageUri);
        activityResult = new Instrumentation.ActivityResult(
                Activity.RESULT_OK, resultData);
    }

    @Test
    public void aPressChangeCheckNewWindow() {
        assertNotNull(withId(R.id.buttonChangeOwner));
        onView(withId(R.id.buttonChangeOwner)).perform(click());
        assertNotNull(withId(R.id.ownerAdd));
    }

    @Test
    public void bEnterInvalidName() {
        onView(withId(R.id.ownerName)).perform(typeText("-Invalidname"), closeSoftKeyboard());
        onView(withId(R.id.ownerError)).check(matches(isDisplayed()));
        onView(withId(R.id.ownerName)).perform(typeText(""), closeSoftKeyboard());
    }

    @Test
    public void cEnterNameAndAddPhotoAndButtonApearsAndSubmit() {
        onView(withId(R.id.ownerName)).perform(typeText(name), closeSoftKeyboard());
=======
import static junit.framework.Assert.assertNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;


@RunWith(AndroidJUnit4.class)

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UseCaseAddOwner {


    // Den truen på slutten der var litt kul. Da starter den ikke Activitien hver gang. Ish.
    @Rule
    public ActivityTestRule mainActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, true);
    private Instrumentation.ActivityResult activityResult;
    private String name = "Snoopey";

    private static String getString(int id) {
        return InstrumentationRegistry.getTargetContext().getString(id);
    }

    @Before
    public void setupImageUri() {
        Resources resources = InstrumentationRegistry.getTargetContext().getResources();
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources
                .getResourcePackageName(
                        R.mipmap.ic_launcher) + '/' + resources.getResourceTypeName(
                R.mipmap.ic_launcher) + '/' + resources.getResourceEntryName(
                R.mipmap.ic_launcher));
        Intent resultData = new Intent();
        resultData.setData(imageUri);
        activityResult = new Instrumentation.ActivityResult(
                Activity.RESULT_OK, resultData);
    }

    @Test
    public void aPressChangeCheckNewWindow() {

        assertNotNull(withId(R.id.button));
        onView(withId(R.id.button)).perform(click());
    }

    @Test
    public void bEnterNameAndAddPhotoAndButtonApearsAndSubmit() {

        onView(withId(R.id.ownerName)).perform(typeText(name), closeSoftKeyboard());

>>>>>>> b0b6ac2d2d5277d618dde87fccd1557014ee018c

        onView(withId(R.id.ownerAdd)).perform(click());

        String chooseFromGallery = getString(R.string.galleryChooseFromGallery);
<<<<<<< HEAD
=======
        setupImageUri();
>>>>>>> b0b6ac2d2d5277d618dde87fccd1557014ee018c

        Intents.init();
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_OPEN_DOCUMENT));
        intending(expectedIntent).respondWith(activityResult);

        onView(withText(chooseFromGallery)).perform(click());
        intended(expectedIntent);
        Intents.release();

        onView(withId(R.id.ownerSubmit)).check(matches(isDisplayed()));
<<<<<<< HEAD
=======
        // tenkte egentlig å ha denne i den neste testen. Men det funket ikke så bra gitt
>>>>>>> b0b6ac2d2d5277d618dde87fccd1557014ee018c
        onView(withId(R.id.ownerSubmit)).perform(click());
    }

    @Test
<<<<<<< HEAD
    public void dCheckThatNameWasSaved() {
        assertNotNull(withText(name));
=======
    public void cCheckThatNameWasSaved() {

        assertNotNull(withText(name));

>>>>>>> b0b6ac2d2d5277d618dde87fccd1557014ee018c
    }
}
