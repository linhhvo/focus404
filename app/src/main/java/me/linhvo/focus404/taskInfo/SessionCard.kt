package me.linhvo.focus404.taskInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.linhvo.focus404.R
import me.linhvo.focus404.database.dao.Session
import me.linhvo.focus404.database.dao.Task
import kotlin.time.Duration

@Composable
fun SessionCard(task: Task, session: Session, deleteSession: suspend (Task, Session) -> Unit) {
    val start = session.startTime.toLocalDateTime(TimeZone.currentSystemDefault())
    val stop = session.stopTime?.toLocalDateTime(TimeZone.currentSystemDefault())
    val duration = session.stopTime?.minus(session.startTime) ?: Duration.ZERO

    val dismissState = rememberSwipeToDismissBoxState()
    val scope = rememberCoroutineScope()

    SwipeToDismissBox(
        state = dismissState,
        modifier = Modifier.fillMaxWidth(),
        enableDismissFromEndToStart = false,
        onDismiss = { direction ->
            if (direction == SwipeToDismissBoxValue.StartToEnd) {
                scope.launch {
                    deleteSession(task, session)
                }
            }
        },
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onTertiary)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    painter = painterResource(R.drawable.delete_icon),
                    contentDescription = "delete session icon",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            val startTime = String.format("%02d:%02d", start.hour, start.minute)
            val stopTime = String.format("%02d:%02d", stop?.hour ?: 0, stop?.minute ?: 0)
            Text(
                text = "${start.date} $startTime - $stopTime",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "${duration.inWholeHours}h${duration.inWholeMinutes % 60}m",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.onBackground)
}