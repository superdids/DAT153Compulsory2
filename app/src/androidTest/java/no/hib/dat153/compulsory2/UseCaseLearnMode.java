package no.hib.dat153.compulsory2;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import no.hib.dat153.compulsory2.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class UseCaseLearnMode {

    @Rule
    public ActivityTestRule mainActivityTestRule = new ActivityTestRule<>(MainActivity.class, true);
    private static final String [] NAMES = { "Didrik", "Ståle", "Viljar" };
    private static final String [] DUMMY_DATA = { "Dummy1", "Dummy2" };


    @Test
    public void testSubmitAllNames() {
        onView(withId(R.id.learn)).perform(click());
        for(int x = 0; x < NAMES.length; x++) {
            //å is not recognized.
            onView(withId(R.id.learnEditText)).perform(replaceText(NAMES[x]));
            onView(withId(R.id.learnButton)).perform(click());
        }
        assertNotNull(withId(R.id.backToMainScreen));
        onView(withId(R.id.backToMainScreen)).perform(click());
    }

    @Test
    public void testSubmitTwoEntriesShowScoreVerifyCorrectAmountOfSubmissions() {
        onView(withId(R.id.learn)).perform(click());
        for(int x = 0; x < DUMMY_DATA.length; x++) {
            onView(withId(R.id.learnEditText)).perform(typeText(DUMMY_DATA[x]), closeSoftKeyboard());
            onView(withId(R.id.learnButton)).perform(click());
        }
        onView(withId(R.id.showScore)).perform(click());
        final String [] resultText = new String[1];
        onView(withId(R.id.scoreResultText)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                resultText[0] = ((TextView) item).getText().toString();
                return true;
            }

            @Override
            public void describeTo(Description description) {
                //do nothing.
            }
        }));

        //splits the text by digits, without removing
        //the separators.
        String [] res = resultText[0].split("(?=\\d+)");
        String toCheck = res[2].charAt(0) + "";
        assertEquals(toCheck, DUMMY_DATA.length + "");


        assertNotNull(withId(R.id.backToMainScreen));
        onView(withId(R.id.backToMainScreen)).perform(click());
    }

}
