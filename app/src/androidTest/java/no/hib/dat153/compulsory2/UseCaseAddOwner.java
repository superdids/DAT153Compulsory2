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

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import no.hib.dat153.compulsory2.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
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

        onView(withId(R.id.ownerAdd)).perform(click());

        String chooseFromGallery = getString(R.string.galleryChooseFromGallery);

        Intents.init();
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_OPEN_DOCUMENT));
        intending(expectedIntent).respondWith(activityResult);

        onView(withText(chooseFromGallery)).perform(click());
        intended(expectedIntent);
        Intents.release();

        onView(withId(R.id.ownerSubmit)).check(matches(isDisplayed()));
        onView(withId(R.id.ownerSubmit)).perform(click());
    }

    @Test
    public void dCheckThatNameWasSaved() {
        assertNotNull(withText(name));
    }
}
