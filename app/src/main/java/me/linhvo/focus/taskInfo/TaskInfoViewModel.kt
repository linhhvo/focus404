package me.linhvo.focus.taskInfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import me.linhvo.focus.common.ApplicationScope
import me.linhvo.focus.database.dao.Session
import me.linhvo.focus.database.dao.Task
import me.linhvo.focus.repository.SessionRepository
import me.linhvo.focus.repository.TaskRepository
import javax.inject.Inject

data class TaskInfoUiState(
    val sessions: List<Session> = emptyList(),
    val showConfirmation: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class TaskInfoViewModel @Inject constructor(
    @ApplicationScope private val appScope: CoroutineScope,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _sessions: MutableStateFlow<List<Session>> = MutableStateFlow(emptyList())
    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _showConfirmation = MutableStateFlow(false)

    val uiState: StateFlow<TaskInfoUiState> =
        combine(_sessions, _errorMessage, _showConfirmation) { sessions, errorMessage, showConfirmation ->
            TaskInfoUiState(
                sessions, showConfirmation, errorMessage
            )
        }.catch {
            emit(TaskInfoUiState(errorMessage = it.message))
        }.stateIn(scope = viewModelScope, started = WhileSubscribed(5000), initialValue = TaskInfoUiState())

    suspend fun getSessionList(task: Task) {
        taskRepository.getSessionsByTask(task.name).collect {
            it?.let { _sessions.value = it.sessions }
        }
    }

    fun deleteTask(task: Task) {
        appScope.launch {
            try {
                taskRepository.deleteTask(task)
            } catch (e: Exception) {
                Log.d("focus_log", "error deleting task ${task.name} -- ${e.message}")
            }
        }
    }

    fun showDialog() {
        _showConfirmation.value = true
    }

    fun hideDialog() {
        _showConfirmation.value = false
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }

    suspend fun deleteSession(task: Task, session: Session) {
        try {
            sessionRepository.deleteSession(session)
        } catch (e: Exception) {
            _errorMessage.value = "failed to delete session: $e"
        }

        try {
            taskRepository.updateTaskDetail(task)
        } catch (e: Exception) {
            _errorMessage.value = "failed to update task: $e"
        }
    }
}