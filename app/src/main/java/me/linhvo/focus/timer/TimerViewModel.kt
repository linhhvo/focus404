package me.linhvo.focus.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.linhvo.focus.database.dao.Session
import me.linhvo.focus.repository.SessionRepository
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Instant

@HiltViewModel
class TimerViewModel @Inject constructor(private val sessionRepository: SessionRepository) : ViewModel() {

    fun stopSession(session: Session, stopTime: Instant = Clock.System.now()) {
        viewModelScope.launch {
            val session = session.copy(stopTime = stopTime)
            sessionRepository.updateSession(session)
        }
    }
}