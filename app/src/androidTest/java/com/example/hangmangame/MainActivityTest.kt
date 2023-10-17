package com.example.hangmangame

import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun checkLetter() {
        onView(withId(R.id.playButton)).perform(click())
        onView(withId(R.id.newGameButton)).perform(click())
        onView(withId(R.id.a)).perform(click())
        onView(withId(R.id.lettersUsedTextView)).check(matches(withText("A")))
    }

    @Test
    fun checkReset() {
        onView(withId(R.id.playButton)).perform(click())
        onView(withId(R.id.newGameButton)).perform(click())
        onView(withId(R.id.a)).perform(click())
        onView(withId(R.id.lettersUsedTextView)).check(matches(withText("A")))
        onView(withId(R.id.newGameButton)).perform(click())
        onView(withId(R.id.lettersUsedTextView)).check(matches(withText("")))
    }
}