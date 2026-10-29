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

    suspend fun updateTaskDetail(latestSession: Session) {
        val task = taskDao.getTaskByName(latestSession.taskName)
        if (task != null) {
            val updatedTask = task.copy(
                totalFocusDuration = task.totalFocusDuration + (latestSession.stopTime?.minus(latestSession.startTime)
                    ?: Duration.ZERO),
                latestSessionEnd = latestSession.stopTime
            )
            taskDao.updateTask(updatedTask)
        } else {
            throw NoSuchElementException("task is not found")
        }
    }
}