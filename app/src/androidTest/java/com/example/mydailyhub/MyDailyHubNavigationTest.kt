package com.example.mydailyhub

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.pressBack
import androidx.compose.ui.test.pressBackUnconditionally
import androidx.compose.ui.test.waitUntil
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class MyDailyHubNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun backFromTasksReturnsToNotesAndPreservesTasksState() = with(composeTestRule) {
        onNodeWithText("Notes").assertIsDisplayed()
        onNodeWithContentDescription("Tasks").performClick()
        onNodeWithText("Tasks").assertIsDisplayed()

        val taskTitle = "Back Button Check"
        onNodeWithTag("tasks_input").performTextInput(taskTitle)
        onNodeWithText("Add task").performClick()
        onNodeWithText(taskTitle).assertExists()

        pressBack()
        onNodeWithText("Notes").assertIsDisplayed()

        onNodeWithContentDescription("Tasks").performClick()
        onNodeWithText(taskTitle).assertExists()
    }

    @Test
    fun backFromCalendarReturnsToTasksAndRestoresCalendarState() = with(composeTestRule) {
        // Navigate to Calendar via Tasks
        onNodeWithContentDescription("Tasks").performClick()
        onNodeWithContentDescription("Calendar").performClick()
        onNodeWithText("Calendar").assertIsDisplayed()

        val monthNode = onNodeWithTag("calendar_month_title")
        val initialMonth = monthNode.fetchSemanticsNode().config[SemanticsProperties.Text]!!.first().text

        onNodeWithContentDescription("Next month").performClick()
        val updatedMonth = monthNode.fetchSemanticsNode().config[SemanticsProperties.Text]!!.first().text
        assertNotEquals(initialMonth, updatedMonth, "Month should change after tapping next")

        pressBack()
        onNodeWithText("Tasks").assertIsDisplayed()

        onNodeWithContentDescription("Calendar").performClick()
        monthNode.assertExists()
        val restoredMonth = monthNode.fetchSemanticsNode().config[SemanticsProperties.Text]!!.first().text
        assertEquals(updatedMonth, restoredMonth)
    }

    @Test
    fun poppingToNotesLeavesNoBackStackEntries() = with(composeTestRule) {
        onNodeWithContentDescription("Tasks").performClick()
        onNodeWithContentDescription("Calendar").performClick()
        onNodeWithContentDescription("Notes").performClick()
        onNodeWithText("Notes").assertIsDisplayed()

        pressBackUnconditionally()
        waitUntil(timeoutMillis = 5_000) {
            activityRule.scenario.state == Lifecycle.State.DESTROYED
        }
        assertEquals(Lifecycle.State.DESTROYED, activityRule.scenario.state)
    }
}
