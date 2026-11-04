package me.linhvo.focus404.taskInfo

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmationDialog(onDismiss: () -> Unit, onConfirmation: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        dismissButton = { TextButton(onClick = { onDismiss() }) { Text("Cancel") } },
        confirmButton = { TextButton(onClick = { onConfirmation() }) { Text("Delete") } },
        text = {
            Text(
                text = "Delete task will also delete all recorded sessions. Are you sure you want to delete this task?",
                style = MaterialTheme.typography.labelMedium, lineHeight = 20.sp
            )
        },
        shape = RoundedCornerShape(8.dp),
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = MaterialTheme.colorScheme.onBackground
    )

}