package me.linhvo.focus404.taskInfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import me.linhvo.focus404.common.ApplicationScope
import me.linhvo.focus404.database.dao.Session
import me.linhvo.focus404.database.dao.Task
import me.linhvo.focus404.repository.SessionRepository
import me.linhvo.focus404.repository.TaskRepository
import javax.inject.Inject

data class TaskInfoUiState(
	val taskState: Task? = null,
	val sessions: List<Session> = emptyList(),
	val showConfirmation: Boolean = false,
	val targetEditSession: Session? = null,
	val errorMessage: String? = null
)

@HiltViewModel
class TaskInfoViewModel @Inject constructor(
	@ApplicationScope private val appScope: CoroutineScope,
	private val sessionRepository: SessionRepository,
	private val taskRepository: TaskRepository
) : ViewModel() {

	private val _task: MutableStateFlow<Task?> = MutableStateFlow(null)
	private val _sessions: MutableStateFlow<List<Session>> = MutableStateFlow(emptyList())
	private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
	private val _showConfirmation = MutableStateFlow(false)
	private val _targetEditSession: MutableStateFlow<Session?> = MutableStateFlow(null)

	val uiState: StateFlow<TaskInfoUiState> =
		combine(
			_task,
			_sessions,
			_errorMessage,
			_showConfirmation,
			_targetEditSession
		) { task, sessions, errorMessage, showConfirmation, targetEditSession ->
			TaskInfoUiState(
				taskState = task,
				sessions = sessions,
				showConfirmation = showConfirmation,
				targetEditSession = targetEditSession,
				errorMessage = errorMessage

			)
		}.catch {
			emit(TaskInfoUiState(errorMessage = it.message))
		}.stateIn(scope = viewModelScope, started = WhileSubscribed(5000), initialValue = TaskInfoUiState())

	suspend fun getTaskState(task: Task) {
		taskRepository.observeTask(task.name).collect {
			Log.d("focus_log", "observe task: $it")
			_task.value = it
		}
	}

	suspend fun getSessionList(task: Task) {
		taskRepository.observeSessionsByTask(task.name).collect {
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

	fun showConfirmationDialog() {
		_showConfirmation.value = true
	}

	fun hideConfirmationDialog() {
		_showConfirmation.value = false
	}

	fun showSessionEditForm(session: Session) {
		_targetEditSession.value = session
	}

	fun hideSessionEditForm() {
		_targetEditSession.value = null
	}

	fun updateSession(task: Task, updatedSession: Session) {
		viewModelScope.launch {
			try {
				sessionRepository.updateSession(updatedSession)
			} catch (e: Exception) {
				_errorMessage.value = "failed to update session: $e"
			}

			try {
				taskRepository.updateTaskDetail(task)
			} catch (e: Exception) {
				_errorMessage.value = "failed to update task: $e"
			}
		}
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