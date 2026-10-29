package me.linhvo.focus.taskInfo

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.linhvo.focus.database.dao.Session
import me.linhvo.focus.database.dao.Task
import me.linhvo.focus.repository.SessionRepository
import me.linhvo.focus.repository.TaskRepository
import javax.inject.Inject

@HiltViewModel
class TaskInfoViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _sessions: MutableStateFlow<List<Session>> = MutableStateFlow(emptyList())
    val sessions = _sessions.asStateFlow()

    suspend fun getSessionList(task: Task) {
        taskRepository.getSessionsByTask(task.name).collect {
            _sessions.value = it.sessions
        }
    }
}