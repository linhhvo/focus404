package me.linhvo.focus404.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddTaskDialog(onSubmit: (String) -> Unit, onDismiss: () -> Unit) {
    var taskName by rememberSaveable { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .height(180.dp)
                .width(300.dp), shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text(text = "Task name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        onSubmit(taskName)
                        onDismiss()
                    }),
                    colors = OutlinedTextFieldDefaults.colors()
                        .copy(
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                            focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                            focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                            cursorColor = MaterialTheme.colorScheme.onBackground
                        ),
                    modifier = Modifier
                        .width(240.dp)
                        .focusRequester(focusRequester)
                )

                val keyboardController = LocalSoftwareKeyboardController.current
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
                        onSubmit(taskName)
                        onDismiss()
                    }) {
                    Text(text = "Add Task")
                }
            }
        }
    }
}