package me.linhvo.focus.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import me.linhvo.focus.database.dao.Task
import me.linhvo.focus.home.HomeScreen
import me.linhvo.focus.taskInfo.TaskInfoScreen
import me.linhvo.focus.timer.TimerScreen

@Serializable
data object Home : NavKey

@Serializable
data class Timer(val task: Task) : NavKey

@Serializable
data class TaskInfo(val task: Task) : NavKey

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(Home)

    Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
        NavDisplay(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            onBack = {
                if (backStack.last() !is Timer) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = entryProvider {
                entry<Home> {
                    HomeScreen(
                        navigateToTaskInfo = { backStack.add(TaskInfo(task = it)) },
                        navigateToTimer = { backStack.add(Timer(task = it)) })
                }
                entry<Timer> { key ->
                    TimerScreen(
                        taskName = key.task,
                        navigateToHome = { backStack.removeLastOrNull() })
                }
                entry<TaskInfo> { key ->
                    TaskInfoScreen(
                        task = key.task,
                        navigateToTimer = { backStack.add(Timer(task = it)) },
                        onBack = { backStack.removeLastOrNull() })
                }

            }
        )
    }
}