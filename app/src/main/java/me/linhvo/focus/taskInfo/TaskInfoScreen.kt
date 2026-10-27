package me.linhvo.focus.taskInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import me.linhvo.focus.R
import me.linhvo.focus.database.dao.Task

@Composable
fun TaskInfoScreen(task: Task, navigateToTimer: (Task) -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = task.name, style = MaterialTheme.typography.headlineLarge)
        Row() {
            OutlinedButton(onClick = { navigateToTimer(task) }) {
                Icon(
                    painter = painterResource(R.drawable.add_session_icon),
                    contentDescription = "start session timer icon"
                )
                Text(text = "Start session", style = MaterialTheme.typography.labelMedium)
            }

            OutlinedButton(onClick = { /*TODO: delete task*/ }) {
                Icon(
                    painter = painterResource(R.drawable.delete_icon),
                    contentDescription = "delete task icon"
                )
                Text(text = "Delete task", style = MaterialTheme.typography.labelMedium)
            }
        }

    }
}