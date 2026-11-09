package me.linhvo.focus404.taskInfo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.linhvo.focus404.R
import me.linhvo.focus404.database.dao.Task
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun TaskInfoScreen(
	task: Task,
	navigateToTimer: (Task, Instant) -> Unit,
	onBack: () -> Unit,
	viewModel: TaskInfoViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	val snackbarHostState = remember { SnackbarHostState() }

	if (uiState.showConfirmation) {
		ConfirmationDialog(onDismiss = { viewModel.hideConfirmationDialog() }, onConfirmation = {
			viewModel.deleteTask(uiState.taskState!!)
			viewModel.hideConfirmationDialog()
			onBack()
		})
	}

	if (uiState.targetEditSession != null) {
		EditSessionForm(
			onDismiss = viewModel::hideSessionEditForm,
			targetSession = uiState.targetEditSession!!,
			onSubmit = {
				viewModel.updateSession(task, it)
				viewModel.hideSessionEditForm()
			}
		)
	}

	LaunchedEffect(uiState.errorMessage) {
		if (uiState.errorMessage != null) {
			val result = snackbarHostState.showSnackbar(
				message = uiState.errorMessage!!,
				withDismissAction = true,
				duration = SnackbarDuration.Long
			)

			if (result == SnackbarResult.Dismissed) {
				viewModel.resetErrorMessage()
			}
		}
	}

	LaunchedEffect(Unit) {
		viewModel.getSessionList(task)
	}

	LaunchedEffect(Unit) {
		viewModel.getTaskState(task)
	}

	Scaffold(
		snackbarHost = {
			SnackbarHost(hostState = snackbarHostState)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding),
			verticalArrangement = Arrangement.Top,
			horizontalAlignment = Alignment.Start
		) {

			Text(
				text = uiState.taskState?.name ?: "",
				style = MaterialTheme.typography.headlineLarge,
				modifier = Modifier.padding(top = 20.dp, start = 20.dp, bottom = 5.dp)
			)
			Text(
				text = "Total focus: ${uiState.taskState?.totalFocusDuration?.inWholeHours}h${(uiState.taskState?.totalFocusDuration?.inWholeMinutes ?: 0) % 60}m",
				style = MaterialTheme.typography.titleMedium,
				modifier = Modifier.padding(start = 20.dp)
			)
			Row(modifier = Modifier.padding(20.dp)) {
				OutlinedButton(
					shape = RoundedCornerShape(6.dp),
					border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.inverseSurface),
					colors = ButtonDefaults.outlinedButtonColors()
						.copy(contentColor = MaterialTheme.colorScheme.onBackground),
					onClick = {
						val startTime = Clock.System.now()
						navigateToTimer(uiState.taskState ?: task, startTime)
					},
					contentPadding = PaddingValues(horizontal = 15.dp, vertical = 12.dp),
				) {
					Icon(
						painter = painterResource(R.drawable.add_session_icon),
						contentDescription = "start session timer icon",
						tint = MaterialTheme.colorScheme.onBackground,
						modifier = Modifier.size(15.dp)
					)
					Text(
						text = "Start session",
						style = MaterialTheme.typography.labelMedium,
						modifier = Modifier.padding(start = 6.dp),
						color = MaterialTheme.colorScheme.onBackground
					)

				}

				OutlinedButton(
					shape = RoundedCornerShape(6.dp),
					border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.inverseSurface),
					colors = ButtonDefaults.outlinedButtonColors()
						.copy(contentColor = MaterialTheme.colorScheme.onBackground),
					onClick = {
						if (uiState.sessions.isNotEmpty()) {
							viewModel.showConfirmationDialog()
						} else {
							viewModel.deleteTask(uiState.taskState ?: task)
							onBack()
						}
					},
					contentPadding = PaddingValues(horizontal = 15.dp, vertical = 12.dp),
					modifier = Modifier.padding(start = 10.dp)
				) {
					Icon(
						painter = painterResource(R.drawable.delete_icon),
						contentDescription = "delete task icon",
						modifier = Modifier.size(15.dp),
						tint = MaterialTheme.colorScheme.onBackground
					)
					Text(
						text = "Delete task",
						style = MaterialTheme.typography.labelMedium,
						modifier = Modifier.padding(start = 6.dp),
						color = MaterialTheme.colorScheme.onBackground
					)
				}
			}

			if (uiState.sessions.isNotEmpty()) {
				HorizontalDivider(
					color = MaterialTheme.colorScheme.onBackground,
					modifier = Modifier.padding(top = 20.dp)
				)
			} else {
				Text(
					text = "No session yet.",
					style = MaterialTheme.typography.labelLarge,
					modifier = Modifier.padding(start = 20.dp)
				)
			}

			LazyColumn {
				items(uiState.sessions) { s ->
					SessionCard(
						task = uiState.taskState ?: task,
						session = s,
						deleteSession = viewModel::deleteSession,
						editSession = viewModel::showSessionEditForm
					)
				}
			}
		}
	}
}