package me.linhvo.focus.timer

import android.content.pm.ActivityInfo
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
import me.linhvo.focus.database.dao.Task

@Composable
fun TimerScreen(taskName: Task, navigateToHome: () -> Unit) {
    val context = LocalActivity.current
    val view = LocalView.current

    LaunchedEffect(Unit) {
        val windowInsetsController = WindowCompat.getInsetsController(context!!.window, view)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    navigateToHome()
                    context?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                })
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "08:17",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}