package me.linhvo.focus.repository

import me.linhvo.focus.database.dao.Task
import me.linhvo.focus.database.dao.TaskDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    suspend fun addTask(taskName: String) = taskDao.addTask(Task(name = taskName))

    fun getAllTasks() = taskDao.getAllTasks()

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    fun getSessionsByTask(taskName: String) = taskDao.getSessionsByTaskStream(taskName)

    suspend fun updateTaskDetail(task: Task) {
        val taskWithSessions = taskDao.getSessionsByTask(task.name)
        if (taskWithSessions != null && taskWithSessions.sessions.isNotEmpty()) {
            val latestSession = taskWithSessions.sessions.maxBy { it.id }

            val updatedTask = task.copy(
                totalFocusDuration = taskWithSessions.sessions.fold(Duration.ZERO) { acc, session ->
                    acc + (session.stopTime?.minus(
                        session.startTime
                    ) ?: Duration.ZERO)
                },
                latestSessionEnd = latestSession.stopTime
            )
            taskDao.updateTask(updatedTask)
        } else {
            val updatedTask = task.copy(totalFocusDuration = Duration.ZERO, latestSessionEnd = null)
            taskDao.updateTask(updatedTask)
        }
    }
}