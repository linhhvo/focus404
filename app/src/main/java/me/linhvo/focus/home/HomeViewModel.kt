package me.linhvo.focus.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import me.linhvo.focus.database.dao.Task
import me.linhvo.focus.repository.SessionRepository
import me.linhvo.focus.repository.TaskRepository
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

data class HomeUiState(
    val taskList: List<Task> = emptyList(),
    val todayFocus: Duration = Duration.ZERO,
    val shouldShowAddTaskForm: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _todayFocus: MutableStateFlow<Duration> = MutableStateFlow(Duration.ZERO)
    private val _shouldShowAddTaskForm = MutableStateFlow(false)


    val uiState: StateFlow<HomeUiState> =
        combine(
            taskRepository.getAllTasks(),
            _errorMessage,
            _todayFocus,
            _shouldShowAddTaskForm
        ) { taskList, error, todayFocus, shouldShowForm ->
            HomeUiState(
                taskList = taskList,
                todayFocus = todayFocus,
                shouldShowAddTaskForm = shouldShowForm,
                errorMessage = error
            )
        }.catch {
            emit(HomeUiState(errorMessage = it.message))
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = HomeUiState()
        )

    fun addTask(taskName: String) {
        viewModelScope.launch {
            try {
                taskRepository.addTask(taskName)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun openAddTaskForm() {
        _shouldShowAddTaskForm.value = true
    }

    fun closeAddTaskForm() {
        _shouldShowAddTaskForm.value = false
    }

    fun startSession(task: Task, startTime: Instant) {
        viewModelScope.launch {
            sessionRepository.startSession(task.name, startTime)
        }
    }


    init {
        viewModelScope.launch {
            val todaySessions = sessionRepository.getSessionsByDate(Clock.System.now())
            _todayFocus.value = todaySessions.fold(Duration.ZERO) { acc, session ->
                acc + (session.stopTime?.minus(session.startTime) ?: Duration.ZERO)
            }
        }

    }
}