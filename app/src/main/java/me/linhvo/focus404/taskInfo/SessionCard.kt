package me.linhvo.focus404.taskInfo

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun SessionCard(
	task: Task,
	session: Session,
	deleteSession: suspend (Task, Session) -> Unit,
	editSession: (Session) -> Unit
) {
	val start = session.startTime.toLocalDateTime(TimeZone.currentSystemDefault())
	val stop = session.stopTime?.toLocalDateTime(TimeZone.currentSystemDefault())
	val duration = session.stopTime?.minus(session.startTime) ?: Duration.ZERO

	val dismissState = rememberSwipeToDismissBoxState()
	val scope = rememberCoroutineScope()

	SwipeToDismissBox(
		state = dismissState,
		modifier = Modifier.fillMaxWidth(),
		onDismiss = { direction ->
			when (direction) {
				SwipeToDismissBoxValue.StartToEnd ->
					scope.launch {
						deleteSession(task, session)
					}

				SwipeToDismissBoxValue.EndToStart -> editSession(session)
				SwipeToDismissBoxValue.Settled -> {}
			}
			scope.launch { dismissState.reset() }
		},
		backgroundContent = {
			val color by animateColorAsState(
				when (dismissState.targetValue) {
					SwipeToDismissBoxValue.StartToEnd ->
						MaterialTheme.colorScheme.secondaryFixed

					SwipeToDismissBoxValue.EndToStart ->
						MaterialTheme.colorScheme.secondaryContainer

					SwipeToDismissBoxValue.Settled -> Color.Transparent
				}
			)

			if (dismissState.targetValue == SwipeToDismissBoxValue.StartToEnd) {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.background(color)
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
			} else if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.background(color)
						.padding(horizontal = 20.dp),
					contentAlignment = Alignment.CenterEnd
				) {
					Icon(
						painter = painterResource(R.drawable.edit_icon),
						contentDescription = "edit session icon",
						tint = MaterialTheme.colorScheme.background,
						modifier = Modifier.size(20.dp)
					)

				}
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