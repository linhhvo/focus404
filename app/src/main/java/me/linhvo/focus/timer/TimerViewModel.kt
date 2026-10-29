package me.linhvo.focus.timer

import android.util.Log
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
import kotlin.time.Clock
import kotlin.time.Instant

@HiltViewModel
class TimerViewModel @Inject constructor(
    @ApplicationScope private val appScope: CoroutineScope,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {
    private val _currentSession: MutableStateFlow<Session?> = MutableStateFlow(null)
    private var sessionStarted = false
    private val _displayTime = MutableStateFlow("")
    val displayTime = _displayTime.asStateFlow()

    fun formatElapsedTime(startTime: Instant) {
        val elapsedTime = Clock.System.now() - startTime
        val m = elapsedTime.inWholeMinutes % 60
        _displayTime.value = String.format("%02d:%02d", elapsedTime.inWholeHours, m)
    }

    fun startSession(task: Task, startTime: Instant) {
        if (!sessionStarted) {
            sessionStarted = true
            viewModelScope.launch {
                val id = sessionRepository.startSession(task.name, startTime)
                _currentSession.value = sessionRepository.getSessionById(id)
                Log.d("focus_log", "start new session: $id")
            }
        }
    }

    fun stopSession(stopTime: Instant = Clock.System.now()) {
        appScope.launch {
            try {
                val session = _currentSession.value!!.copy(stopTime = stopTime)
                sessionRepository.updateSession(session)
                taskRepository.updateTaskDetail(session)
            } catch (e: Exception) {
                throw Exception("failed to stop session: ${e.message}")
            }
        }
    }
}