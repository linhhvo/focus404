package me.linhvo.focus.timer

import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.delay
import me.linhvo.focus.database.dao.Task
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

@Composable
fun TimerScreen(taskName: Task, startTime: Instant, navigateToHome: () -> Unit) {
    val context = LocalActivity.current
    val view = LocalView.current
    val window = context?.window ?: return

    var elapsedTime by remember { mutableStateOf(Duration.ZERO) }
    var displayTime by remember { mutableStateOf("") }
    var isRunning = true

    LaunchedEffect(Unit) {
        val windowInsetsController = WindowCompat.getInsetsController(window, view)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val layoutParams = window.attributes
        layoutParams.screenBrightness = 0.05f
        window.attributes = layoutParams
    }

    LaunchedEffect(elapsedTime, isRunning) {
        while (isRunning) {
            elapsedTime = Clock.System.now() - startTime
            val m = elapsedTime.inWholeMinutes % 60
            displayTime = String.format("%02d:%02d", elapsedTime.inWholeHours, m)
            delay(1.minutes)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    //TODO: stop session
                    isRunning = false
                    navigateToHome()
                    context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                })
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayTime,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}