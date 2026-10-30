package me.linhvo.focus.taskInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.linhvo.focus.R
import me.linhvo.focus.database.dao.Session
import kotlin.time.Duration

@Composable
fun SessionCard(session: Session, deleteSession: (Session) -> Unit) {
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
                deleteSession(session)
            }
        },
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onTertiary)
                    .padding(horizontal = 12.dp),
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
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = "${start.date} ${start.hour}:${start.minute} - ${stop?.hour}:${stop?.minute}",
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