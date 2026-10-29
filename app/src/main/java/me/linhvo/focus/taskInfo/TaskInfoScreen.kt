package me.linhvo.focus.taskInfo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.linhvo.focus.R
import me.linhvo.focus.database.dao.Task
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun TaskInfoScreen(
    task: Task,
    navigateToTimer: (Task, Instant) -> Unit,
    onBack: () -> Unit,
    viewModel: TaskInfoViewModel = hiltViewModel()
) {
    val sessions = viewModel.sessions.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getSessionList(task)
    }

    Scaffold(modifier = Modifier.padding(20.dp)) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = task.name, style = MaterialTheme.typography.headlineLarge)
            Row(modifier = Modifier.padding(vertical = 20.dp)) {
                OutlinedButton(
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.inverseSurface),
                    colors = ButtonDefaults.outlinedButtonColors()
                        .copy(contentColor = MaterialTheme.colorScheme.onBackground),
                    onClick = {
                        val startTime = Clock.System.now()
                        navigateToTimer(task, startTime)
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
                        modifier = Modifier.padding(start = 6.dp)
                    )

                }

                OutlinedButton(
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.inverseSurface),
                    colors = ButtonDefaults.outlinedButtonColors()
                        .copy(contentColor = MaterialTheme.colorScheme.onBackground),
                    onClick = { /*TODO: delete task*/ },
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 12.dp),
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.delete_icon),
                        contentDescription = "delete task icon",
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "Delete task",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }

            LazyColumn {
                items(sessions.value) { s ->
                    SessionCard(s)
                }
            }
        }
    }
}