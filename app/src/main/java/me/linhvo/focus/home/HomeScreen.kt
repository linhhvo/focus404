package me.linhvo.focus.home

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@Composable
fun HomeScreen(
    navigateToTaskInfo: (Task) -> Unit,
    navigateToTimer: (Task) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.shouldShowAddTaskForm) {
        AddTaskDialog(onDismiss = viewModel::closeAddTaskForm, onSubmit = viewModel::addTask)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Today focus: ${uiState.todayFocus.inWholeHours}h${uiState.todayFocus.inWholeMinutes}m",
                style = MaterialTheme.typography.headlineLarge,
            )
        }

        val haptics = LocalHapticFeedback.current
        //TODO: need to sort by most recent task
        LazyVerticalStaggeredGrid(
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
                            navigateToTimer(task)
                        },
                        onLongClickLabel = "start focus timer for the selected task"
                    ),
                )
            }

        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        ExtendedFloatingActionButton(
            modifier = Modifier.padding(vertical = 40.dp, horizontal = 30.dp),
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
    }
}