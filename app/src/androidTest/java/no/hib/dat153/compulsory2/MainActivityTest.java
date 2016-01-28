package no.hib.dat153.compulsory2;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import no.hib.dat153.compulsory2.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testTextViewChanged() {
        enterText("Ole");
        onView(withId(R.id.addPerson)).perform(click());
    }

    private static ViewInteraction enterText(String text) {
        return onView(withId(R.id.personName)).perform(typeText(text), closeSoftKeyboard());
    }
}
