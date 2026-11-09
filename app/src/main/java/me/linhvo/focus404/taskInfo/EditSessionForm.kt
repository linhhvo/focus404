package me.linhvo.focus404.taskInfo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import me.linhvo.focus404.database.dao.Session
import kotlin.math.min
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSessionForm(
	onDismiss: () -> Unit,
	targetSession: Session,
	onSubmit: (Session) -> Unit,
) {
	val start = targetSession.startTime.toLocalDateTime(TimeZone.currentSystemDefault())
	val stop = targetSession.stopTime?.toLocalDateTime(TimeZone.currentSystemDefault())

	val startTimePicketState = rememberTimePickerState(
		initialHour = start.hour,
		initialMinute = start.minute,
		is24Hour = true
	)

	val stopTimePicketState = rememberTimePickerState(
		initialHour = stop?.hour ?: 0,
		initialMinute = stop?.minute ?: 0,
		is24Hour = true
	)

	val timePickerColors = TimePickerDefaults.colors().copy(
		timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.background,
		timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onBackground,
	)

	var showError by remember { mutableStateOf(false) }

	if (showError) {
		Dialog(onDismissRequest = { showError = false }) {
			Card(shape = RoundedCornerShape(8.dp)) {
				Column(
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					Text(
						text = "start time cannot be after stop time",
						style = MaterialTheme.typography.labelMedium,
						modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
					)
					TextButton(onClick = { showError = false }) {
						Text(text = "OK")
					}
				}
			}
		}
	}


	Dialog(onDismissRequest = { onDismiss() }) {
		Card(shape = RoundedCornerShape(8.dp)) {
			Column(
				modifier = Modifier
					.wrapContentSize()
					.width(IntrinsicSize.Max)
					.padding(20.dp), horizontalAlignment = Alignment.Start
			) {
				Text(text = "Edit session ${targetSession.id}", style = MaterialTheme.typography.titleMedium)

				Text(
					text = "Start time",
					style = MaterialTheme.typography.labelMedium,
					modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
				)
				TimeInput(
					state = startTimePicketState,
					colors = timePickerColors,
				)

				Text(
					text = "Stop time",
					style = MaterialTheme.typography.labelMedium,
					modifier = Modifier.padding(bottom = 10.dp)
				)
				TimeInput(state = stopTimePicketState, colors = timePickerColors)

				Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
					val keyboardController = LocalSoftwareKeyboardController.current
					Button(
						shape = RoundedCornerShape(5.dp),
						colors = ButtonColors(
							containerColor = MaterialTheme.colorScheme.primary,
							contentColor = MaterialTheme.colorScheme.inverseOnSurface,
							disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant,
							disabledContentColor = MaterialTheme.colorScheme.onSurface
						),
						modifier = Modifier.padding(end = 10.dp),
						onClick = {
							val newStartTime =
								convertInputTimeToInstant(start, startTimePicketState.hour, startTimePicketState.minute)
							val newStopTime =
								convertInputTimeToInstant(
									stop ?: start,
									stopTimePicketState.hour,
									stopTimePicketState.minute
								)
							if (newStartTime < newStopTime) {
								val updatedSession =
									targetSession.copy(startTime = newStartTime, stopTime = newStopTime)
								onSubmit(updatedSession)
							} else {
								showError = true
							}
							keyboardController?.hide()
						}) { Text(text = "Save") }

					Button(
						shape = RoundedCornerShape(5.dp),
						colors = ButtonColors(
							containerColor = MaterialTheme.colorScheme.inverseSurface,
							contentColor = MaterialTheme.colorScheme.inverseOnSurface,
							disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant,
							disabledContentColor = MaterialTheme.colorScheme.onSurface
						),
						onClick = {
							keyboardController?.hide()
							onDismiss()
						}) { Text(text = "Cancel") }


				}
			}
		}
	}
}

fun convertInputTimeToInstant(ogTime: LocalDateTime, inputHour: Int, inputMinute: Int): Instant {
	val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
	val newTime = LocalDateTime(
		year = ogTime.year,
		month = ogTime.month,
		day = ogTime.day,
		hour = min(inputHour, now.hour),
		minute = if (inputHour == now.hour) min(inputMinute, now.minute) else inputMinute,
	)
	return newTime.toInstant(TimeZone.currentSystemDefault())
}