package no.hib.dat153.compulsory2;


import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import no.hib.dat153.compulsory2.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testPushingAddButtonNotPromptingDialogUponEmptyInput() {
        String dialogTitle = getString(R.string.galleryAddPhoto);
        onView(withId(R.id.addPerson)).perform(click());
        onView(withText(dialogTitle)).check(doesNotExist());
    }

    @Test
    public void testInputFieldChanged() {
        String input = "Ole";
        onView(withId(R.id.personName)).perform(typeText(input), closeSoftKeyboard());
        onView(withId(R.id.addPerson)).perform(click());

        String cancelButton = getString(R.string.galleryCancel);

        onView(withText(cancelButton)).perform(click());
        onView(withId(R.id.personName)).check(matches(withText(input)));
    }

    @Test
    public void testPushingNamesButtonPressBackToMain() {
        onView(withId(R.id.names)).perform(click());
        onView(withId(R.id.nameButtonHome)).perform(click());
    }

    @Test
    public void testPushingImagesButtonPressBackToMain() {
        onView(withId(R.id.images)).perform(click());
        onView(withId(R.id.imageButtonHome)).perform(click());
    }

    @Test
    public void testPushingLearnButtonPressBackToMain() {
        onView(withId(R.id.learn)).perform(click());
        onView(withId(R.id.learnBackToMain)).perform(click());
    }

    private static String getString(int id) {
        return InstrumentationRegistry.getTargetContext().getString(id);
    }
}
