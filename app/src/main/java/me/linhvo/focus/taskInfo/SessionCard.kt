package me.linhvo.focus.taskInfo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.linhvo.focus.R
import me.linhvo.focus.database.dao.Session

@Composable
fun SessionCard(session: Session) {
    val start = session.startTime.toLocalDateTime(TimeZone.currentSystemDefault())
    val stop = session.stopTime?.toLocalDateTime(TimeZone.currentSystemDefault())

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(text = "${start.date}")
            Text(text = "${start.hour}:${start.minute} - ${stop?.hour}:${stop?.minute}")
        }

        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.delete_icon),
                contentDescription = "delete session icon",
                modifier = Modifier.size(15.dp)
            )
        }
    }

}