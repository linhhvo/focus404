package me.linhvo.focus.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.linhvo.focus.database.dao.Task

@Composable
fun TaskCard(index: Int, task: Task) {
    val colors = listOf(
        Pair(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary),
        Pair(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary),
        Pair(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.onTertiary),
        Pair(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer),
        Pair(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer),
        Pair(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer),
        Pair(MaterialTheme.colorScheme.primaryFixed, MaterialTheme.colorScheme.onPrimaryFixed),
//        Pair(MaterialTheme.colorScheme.onPrimary, MaterialTheme.colorScheme.primary),
    )

    val pos = index % colors.size

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(color = colors[pos].first),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = task.name,
                style = MaterialTheme.typography.titleLarge,
                color = colors[pos].second
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "${task.totalFocusDuration.inWholeHours}h${task.totalFocusDuration.inWholeMinutes}m",
                style = MaterialTheme.typography.titleMedium,
                color = colors[pos].second
            )
        }

    }
}