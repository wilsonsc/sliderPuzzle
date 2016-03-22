package scottnickamanda.sliderpuzzle;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests MainActivity
 *
 * Created by Amanda Buhr on 3/19/16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkSelectImageButtonClickable() {
        onView(withId(R.id.selectImage)).check(matches(isClickable()));
    }

    @Test
    public void checkSelectSizeClickable() {
        onView(withId(R.id.selectSize)).check(matches(isClickable()));
    }

    @Test
    public void checkSelectSize3x3() {
        onView(withId(R.id.selectSize)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("3x3"))).perform(click());
        onView(withId(R.id.selectSize)).check(matches(withSpinnerText(containsString("3x3"))));
    }

    @Test
    public void checkSelectSize4by4() {
        onView(withId(R.id.selectSize)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("4x4"))).perform(click());
        onView(withId(R.id.selectSize)).check(matches(withSpinnerText(containsString("4x4"))));
    }

    @Test
    public void checkSelectSize5by5() {
        onView(withId(R.id.selectSize)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("5x5"))).perform(click());
        onView(withId(R.id.selectSize)).check(matches(withSpinnerText(containsString("5x5"))));
    }

    @Test
    public void checkStartGameClickable() {
        onView(withId(R.id.newGame)).check(matches(isClickable()));
    }

    @Test
    public void checkCurrentImageDisplayed() {
        onView(withId(R.id.currentImage)).check(matches(isDisplayed()));
    }

    @Test
    public void checkMonkeyImageDisplayedAfterClicked() {
        onView(withId(R.id.selectImage)).perform(click());
        onData(anything())
                .inAdapterView(allOf(withId(R.id.imageSelect), isDisplayed()))
                .atPosition(1)
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.currentImage)).check(matches(isDisplayed()));
    }

    @Test
    public void checkLemursImageDisplayedAfterClicked() {
        onView(withId(R.id.selectImage)).perform(click());
        onData(anything())
                .inAdapterView(allOf(withId(R.id.imageSelect), isDisplayed()))
                .atPosition(2)
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.currentImage)).check(matches(isDisplayed()));
    }

    @Test
    public void checkTigerImageDisplayedAfterClicked() {
        onView(withId(R.id.selectImage)).perform(click());
        onData(anything())
                .inAdapterView(allOf(withId(R.id.imageSelect), isDisplayed()))
                .atPosition(3)
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.currentImage)).check(matches(isDisplayed()));
    }

    @Test
    public void checkFallsImageDisplayedAfterClicked() {
        onView(withId(R.id.selectImage)).perform(click());
        onData(anything())
                .inAdapterView(allOf(withId(R.id.imageSelect), isDisplayed()))
                .atPosition(4)
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.currentImage)).check(matches(isDisplayed()));
    }

    @Test
    public void checkSpringsImageDisplayedAfterClicked() {
        onView(withId(R.id.selectImage)).perform(click());
        onData(anything())
                .inAdapterView(allOf(withId(R.id.imageSelect), isDisplayed()))
                .atPosition(5)
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.currentImage)).check(matches(isDisplayed()));
    }

    @Test
    public void checkCatImageDisplayedAfterClicked() {
        onView(withId(R.id.selectImage)).perform(click());
        onData(anything())
                .inAdapterView(allOf(withId(R.id.imageSelect), isDisplayed()))
                .atPosition(0)
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.currentImage)).check(matches(isDisplayed()));
    }

    @Test
    public void checkStartGameLaunch() {
        onView(withId(R.id.newGame)).perform(click());
        onView(withId(R.id.movesMade)).check(matches(isDisplayed()));
        onView(withId(R.id.gridView)).check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    public void checkSelectImageLaunch() {
        onView(withId(R.id.selectImage)).perform(click());
        onView(withId(R.id.imageSelect)).check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    public void checkMovesCounter() {
        onView(withId(R.id.newGame)).perform(click());
        onView(withId(R.id.movesMade)).check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    public void checkResetDisplayed() {
        onView(withId(R.id.newGame)).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Reset Game")).check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    public void checkMove() {
        onView(withId(R.id.newGame)).perform(click());
        onView(withId(R.id.gridView)).perform(click());
        pressBack();
    }

    @Test
    public void checkResetWorks() {
        onView(withId(R.id.newGame)).perform(click());

        onView(withId(R.id.gridView)).perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Reset Game")).perform(click());

        onView(withText(R.string.zeroMoves)).check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    public void checkIncrementMoveCounterWorks() {
        onView(withId(R.id.newGame)).perform(click());
        for (int j=0;j<100;j++) {
            for (int i = 0; i < 9; i++) {
                onData(anything())
                        .inAdapterView(allOf(withId(R.id.gridView), isDisplayed()))
                        .atPosition(i)
                        .check(matches(isDisplayed()))
                        .perform(click());
            }
        }
//        onView(withId(R.id.gridView)).perform(click());
        onView(withId(R.string.zeroMoves)).check(doesNotExist());
        pressBack();
    }

    @Test
    public void checkReturnToMenuDisplayed() {
        onView(withId(R.id.newGame)).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Return to Menu")).check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    public void checkReturnToMenuWorks() {
        onView(withId(R.id.newGame)).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Return to Menu")).perform(click());
        onView(withId(R.id.selectImage)).check(matches(isDisplayed()));
        onView(withId(R.id.currentImage)).check(matches(isDisplayed()));
        onView(withId(R.id.selectSize)).check(matches(isDisplayed()));
    }
}