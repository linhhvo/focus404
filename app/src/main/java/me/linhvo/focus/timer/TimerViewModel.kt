package me.linhvo.focus.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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
import kotlin.time.Clock
import kotlin.time.Instant

@HiltViewModel(assistedFactory = TimerViewModel.Factory::class)
class TimerViewModel @AssistedInject constructor(
    @ApplicationScope private val appScope: CoroutineScope,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository,
    @Assisted val task: Task,
    @Assisted val startTime: Instant
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(task: Task, startTime: Instant): TimerViewModel
    }

    private val _currentSession: MutableStateFlow<Session?> = MutableStateFlow(null)
    private val _displayTime = MutableStateFlow("")
    val displayTime = _displayTime.asStateFlow()

    fun formatElapsedTime() {
        val elapsedTime = Clock.System.now() - startTime
        val m = elapsedTime.inWholeMinutes % 60
        _displayTime.value = String.format("%02d:%02d", elapsedTime.inWholeHours, m)
    }

    fun stopSession(stopTime: Instant = Clock.System.now()) {
        appScope.launch {
            try {
                val session = _currentSession.value!!.copy(stopTime = stopTime)
                sessionRepository.updateSession(session)
                taskRepository.updateTotalFocusDuration(session)
            } catch (e: Exception) {
                throw Exception("failed to stop session: ${e.message}")
            }
        }
    }

    init {
        viewModelScope.launch {
            val id = sessionRepository.startSession(task.name, startTime)
            _currentSession.value = sessionRepository.getSessionById(id)
        }
    }
}