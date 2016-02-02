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
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import no.hib.dat153.compulsory2.activities.AddOwnerActivity;
import no.hib.dat153.compulsory2.activities.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;


/**
 * Testing the add person use-case.
 * @author Didrik Emil Aubert
 * @author Viljar Buen Rolfsen
 * @author Ståle André Mikalsen
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UseCaseAddPerson {

    private Instrumentation.ActivityResult activityResult;
    //Assuming ''Ole'' is not existing in the database.
    private String name = "Ole";
    private static final int [] pos = new int[1];

    @Rule
    public ActivityTestRule mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Stubs the gallery intent upon choosing an image from gallery.
     */
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
    public void aAddPersonCheckIfExists() {
        String input = name;
        onView(withId(R.id.personName)).perform(typeText(input), closeSoftKeyboard());

        onView(withId(R.id.addPerson)).perform(click());

        String chooseFromGallery = getString(R.string.galleryChooseFromGallery);
        setupImageUri();

        Intents.init();
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_OPEN_DOCUMENT));
        intending(expectedIntent).respondWith(activityResult);

        onView(withText(chooseFromGallery)).perform(click());
        intended(expectedIntent);
        Intents.release();

        String dialogTitle = getString(R.string.galleryAddPhoto);
        onView(withId(R.id.addPerson)).perform(click());
        onView(withText(dialogTitle)).check(doesNotExist());
    }


    @Test
    public void bOpenNamesAndCheckNewImage(){
        onView(withId(R.id.names)).perform(click());
        onView(withText(name)).perform(click());
        onView(withId(R.id.nameOfPerson)).check(matches(withText(name)));
    }

    @Test
    public void cOpenImagesAndCheckNewImage() {
        onView(withId(R.id.images)).perform(click());

        onView(withId(R.id.imageListView)).check(matches(callbacks));

        onData(anything()).inAdapterView(allOf(withId(R.id.imageListView)))
                .atPosition(pos[0]).perform(click());
        onView(withId(R.id.nameOfPerson)).check(matches(withText(name)));
    }

    @Test
    public void dDeletePerson() {
        onView(withId(R.id.names)).perform(click());
        onView(allOf(withText(R.string.delete), hasSibling(withText(name))))
                .perform(click());
        onView(withText(name)).check(doesNotExist());
    }

    @Test
    public void eRemoveAllEntriesAndVerifyNothingToClick() {
        onView(withId(R.id.images)).perform(click());

        //final int [] pos = new int[1];
        for(int x = 0; x < 3; x++) {
            onView(withId(R.id.imageListView)).check(matches(callbacks));
            onData(anything()).inAdapterView(allOf(withId(R.id.imageListView),
                    hasSibling(withText(R.string.delete))))
                   .atPosition(pos[0]).perform(click());
            //onView(allOf(withId(R.id.imageListView), hasSibling(withText(R.string.delete)))
        }
        onView(withId(R.id.imageListView)).check(matches(callbacks));
        assertEquals(pos[0], -1);
    }

    private static String getString(int id) {
        return InstrumentationRegistry.getTargetContext().getString(id);
    }

    private Matcher<View> callbacks = new TypeSafeMatcher<View>() {
        @Override
        protected boolean matchesSafely(View item) {
            pos[0] = ((ListView) item).getCount() - 1;
            return true;
        }

        @Override
        public void describeTo(Description description) {
            //Do nothing.
        }
    };
}
