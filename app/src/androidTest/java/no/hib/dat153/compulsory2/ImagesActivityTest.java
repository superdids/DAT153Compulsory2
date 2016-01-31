package no.hib.dat153.compulsory2;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import no.hib.dat153.compulsory2.activities.ImagesActivity;
import no.hib.dat153.compulsory2.adapters.ImageAdapter;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


@RunWith(AndroidJUnit4.class)
public class ImagesActivityTest {

    @Rule
    public ActivityTestRule imagesActivityTestRule = new ActivityTestRule<>(ImagesActivity.class);

    @Test
    public void pressFirstImageInEntry() {
        onData(is(instanceOf(ImageAdapter.class))).atPosition(0).check(matches(
                withText("Viljar")));
    }
}
