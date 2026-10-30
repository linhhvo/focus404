package me.linhvo.focus.taskInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.linhvo.focus.common.ApplicationScope
import me.linhvo.focus.database.dao.Session
import me.linhvo.focus.database.dao.Task
import me.linhvo.focus.repository.SessionRepository
import me.linhvo.focus.repository.TaskRepository
import javax.inject.Inject

@HiltViewModel
class TaskInfoViewModel @Inject constructor(
    @ApplicationScope private val appScope: CoroutineScope,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _sessions: MutableStateFlow<List<Session>> = MutableStateFlow(emptyList())
    val sessions = _sessions.asStateFlow()

    suspend fun getSessionList(task: Task) {
        taskRepository.getSessionsByTask(task.name).collect {
            it?.let { _sessions.value = it.sessions }
        }
    }

    fun deleteTask(task: Task) {
        appScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun deleteSession(session: Session) {
        viewModelScope.launch {
            sessionRepository.deleteSession(session)
        }
    }
}