import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.dice.MainActivity
import com.example.dice.R

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivity2Test {

    private var myAddButton = 0
    private var mySubtractButton = 0
    private var myRollButton = 0
    private var myResetButton = 0
    private var myScoreTextField = 0

    @Before
    fun initValidString() {
        // Please set your id names here.
        myAddButton = (R.id.addButton)
        mySubtractButton = R.id.subtractButton
        myRollButton = R.id.rollButton
        myResetButton = R.id.resetButton
        myScoreTextField = R.id.textViewScore
    }

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun clickRollAddButton3Times() {
        // Set a fixed seed for the random number generator
        val random = Random(1)

        // Reset initial state
        onView(withId(myResetButton)).perform(click())

        // Perform the sequence of clicks: roll, add, roll, add, roll, add
        for (i in 1..3) {
            onView(withId(myRollButton)).perform(click())

            val diceNumber = random.nextInt(1, 7)
            onView(withId(myScoreTextField)).perform(click())
            onView(withId(myAddButton)).perform(click())

            // Assuming your score updates correctly after each roll and add
            // If not, you might need to add a delay or an idling resource
        }

        // Check if the score becomes 10
        onView(withId(myScoreTextField)).check(matches(withText("10")))
    }
    @Test

    fun clickRollAddSubtractButton3Times() {
        // Set a fixed seed for the random number generator

        val random = Random(1)
        val addButton = onView(withId(R.id.addButton))
        val subtractButton = onView(withId(R.id.subtractButton))
        val rollButton = onView(withId(R.id.rollButton))

        // Perform the sequence of clicks: roll, add, roll, subtract, roll, add
        rollButton.perform(click())
        addButton.perform(click())
        rollButton.perform(click())
        subtractButton.perform(click())
        rollButton.perform(click())
        addButton.perform(click())

        // Check if the score becomes 8
        val textView = onView(withId(R.id.textViewScore))
        textView.check(matches(withText("8")))
    }

    @Test
    fun testLowerLimitsOfScore() {
        // given: open app
        // when: click add, subtract, subtract
        // then: score is 0

        val addButton = onView(withId(myAddButton))
        val subtractButton = onView(withId(mySubtractButton))
        val rollButton = onView(withId(myRollButton))
        val resetButton= onView(withId(myResetButton))


        rollButton.perform(click())
        addButton.perform(click())
        rollButton.perform(click())
        subtractButton.perform(click())
        rollButton.perform(click())
        subtractButton.perform(click())
        rollButton.perform(click())
        subtractButton.perform(click())
        rollButton.perform(click())
        subtractButton.perform(click())
        rollButton.perform(click())
        subtractButton.perform(click())
        rollButton.perform(click())
        subtractButton.perform(click())
        rollButton.perform(click())
        subtractButton.perform(click())
        val textView = onView(withId(myScoreTextField))
        textView.check(matches(withText("0")))
    }

    @Test
    fun testResetButton() {
        // given: open app
        // when: click roll/add 3 times and reset 1 time
        // then: score is 0
        val addButton = onView(withId(myAddButton))
        val resetButton = onView(withId(myResetButton))
        val rollButton = onView(withId(myRollButton))

        for (i in 1..3) {
            rollButton.perform(click())
            addButton.perform(click())
        }

        resetButton.perform(click())

        val textView = onView(withId(myScoreTextField))
        textView.check(matches(withText("0")))
    }

    @Test
    fun testScoreOnRotation() {
        // given: open app
        // when: click roll/add 3 times and rotate device
        // then: score is 10
        val addButton = onView(withId(myAddButton))
        val rollButton = onView(withId(myRollButton))

        for (i in 1..3) {
            rollButton.perform(click())
            addButton.perform(click())
        }

        val textView = onView(withId(myScoreTextField))
        textView.check(matches(withText("10")))

        mActivityScenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        textView.check(matches(withText("10")))

    }

    @Test
    fun testScoreOnRotationWithClick() {
        // given: open app
        // when: click roll/add 2 times and rotate device, click roll/add
        // then: score is 10
        val addButton = onView(withId(myAddButton))
        val rollButton = onView(withId(myRollButton))

        for (i in 1..2) {
            rollButton.perform(click())
            addButton.perform(click())
        }

        val textView = onView(withId(myScoreTextField))
        textView.check(matches(withText("5")))

        mActivityScenarioRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        rollButton.perform(click())
        addButton.perform(click())
        val textView2 = onView(withId(myScoreTextField))
        textView2.check(matches(withText("10")))

    }


    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}