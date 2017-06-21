package com.example.uadnd.cou8901.bakersresourceapp.ui;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.uadnd.cou8901.bakersresourceapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.uadnd.cou8901.bakersresourceapp.R.id.rv_recipes;

/**
 * Created by dd2568 on 6/18/2017.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeMainActivityTest {

    @Rule
    public ActivityTestRule<RecipeMainActivity> mActivityTestRule =
            new ActivityTestRule<RecipeMainActivity>(RecipeMainActivity.class);


    @Test
    public void navigateToRecipeStepDetail1() {


             // Assert Recipe Main Activity is displayed
            onView(withId(R.id.rv_recipes)).check(matches(isDisplayed()));

            //Peform   two clicks()
            onView(withText("1. Nutella Pie")).perform(click());
             onView(withText("Recipe Introduction")).perform(click());

              //Assert Step Text

             onView(withId(R.id.step_detail)).check(matches(withText("Recipe Introduction")));
    }

    @Test
    public void navigateToRecipeStepDetail3() {

        // Assert Recipe Main Activity is displayed
        onView(withId(R.id.rv_recipes)).check(matches(isDisplayed()));

        //Perform swipeUps and clicks
        onView(withId(R.id.rv_recipes)).perform(swipeUp());
        onView(withId(R.id.rv_recipes)).perform(swipeUp());
        onView(withId(R.id.rv_recipes)).perform(swipeUp());
        onView(withText("3. Yellow Cake")).perform(click());
        onView(withText("Recipe Introduction")).perform(click());

        //Assert Step Text
        onView(withId(R.id.step_detail)).check(matches(withText("Recipe Introduction")));

    }
}
