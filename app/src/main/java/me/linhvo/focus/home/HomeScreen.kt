package me.linhvo.focus.home

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.linhvo.focus.R
import me.linhvo.focus.database.dao.Task
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun HomeScreen(
    navigateToTaskInfo: (Task) -> Unit,
    navigateToTimer: (Task, Instant) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.shouldShowAddTaskForm) {
        AddTaskDialog(onDismiss = viewModel::closeAddTaskForm, onSubmit = viewModel::addTask)
    }

    val gridState = rememberLazyStaggeredGridState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.openAddTaskForm() },
                shape = RoundedCornerShape(8.dp),
                containerColor = MaterialTheme.colorScheme.inverseSurface,
                icon = {
                    Icon(
                        painter = painterResource((R.drawable.plus_icon)),
                        tint = MaterialTheme.colorScheme.inverseOnSurface,
                        contentDescription = "plus icon"
                    )
                },
                text = {
                    Text(
                        text = "Add Task",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp, start = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Today focus: ${uiState.todayFocus.inWholeHours}h${uiState.todayFocus.inWholeMinutes % 60}m",
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp, start = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${uiState.sinceLastSession.inWholeHours}h${uiState.sinceLastSession.inWholeMinutes % 60}m since last session",
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            val haptics = LocalHapticFeedback.current
            LazyVerticalStaggeredGrid(
                state = gridState,
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(uiState.taskList) { index, task ->
                    TaskCard(
                        index,
                        task,
                        Modifier.combinedClickable(
                            onClick = { navigateToTaskInfo(task) },
                            onClickLabel = "open task info screen to manage task and view list of sessions",
                            onLongClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                val startTime = Clock.System.now()
                                navigateToTimer(task, startTime)
                            },
                            onLongClickLabel = "start focus timer for the selected task"
                        ),
                    )
                }

            }
        }
    }
}