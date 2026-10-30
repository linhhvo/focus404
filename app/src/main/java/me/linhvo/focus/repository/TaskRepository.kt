package me.linhvo.focus.repository

import me.linhvo.focus.database.dao.Session
import me.linhvo.focus.database.dao.Task
import me.linhvo.focus.database.dao.TaskDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    suspend fun addTask(taskName: String) = taskDao.addTask(Task(name = taskName))

    fun getAllTasks() = taskDao.getAllTasks()

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    fun getSessionsByTask(taskName: String) = taskDao.getSessionsByTask(taskName)

    suspend fun updateTaskDetail(newSession: Session) {
        val task = taskDao.getTaskByName(newSession.taskName)
        if (task != null) {
            val updatedTask = task.copy(
                totalFocusDuration = task.totalFocusDuration + (newSession.stopTime?.minus(newSession.startTime)
                    ?: Duration.ZERO),
                latestSessionEnd = newSession.stopTime
            )
            taskDao.updateTask(updatedTask)
        } else {
            throw NoSuchElementException("task is not found")
        }
    }
}