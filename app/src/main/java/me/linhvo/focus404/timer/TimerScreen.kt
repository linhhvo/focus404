package me.linhvo.focus404.timer

import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import me.linhvo.focus404.database.dao.Task
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

@Composable
fun TimerScreen(
    task: Task,
    startTime: Instant,
    navigateToHome: () -> Unit,
    viewModel: TimerViewModel = hiltViewModel()
) {
    val context = LocalActivity.current
    val view = LocalView.current
    val window = context?.window ?: return

    val displayTime = viewModel.displayTime.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        val windowInsetsController = WindowCompat.getInsetsController(window, view)
        // hide status bar and navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        // system bars can be temporarily revealed by swiping from edge but will hide automatically
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // rotate screen to landscape
        context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        // keep display always on while timer is running
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // reduce display brightness
        val layoutParams = window.attributes
        layoutParams.screenBrightness = 0.05f
        window.attributes = layoutParams

        // start new session
        viewModel.startSession(task, startTime)
        while (true) {
            viewModel.formatElapsedTime(startTime)
            delay(1.minutes)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    viewModel.stopSession(task)
                    context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    navigateToHome()
                })
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayTime.value,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}