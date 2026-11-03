package me.linhvo.focus.database.dao

import androidx.room3.*
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Instant

@Entity(indices = [Index(value = ["name"], unique = true)])
@Serializable
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val totalFocusDuration: Duration = Duration.ZERO,
    val latestSessionEnd: Instant? = null
)

data class TaskWithSessions(
    @Embedded val task: Task,
    @Relation(parentColumns = ["name"], entityColumns = ["taskName"])
    val sessions: List<Session>
)

@Dao
interface TaskDao {
    @Query("select * from task where name = :taskName")
    suspend fun getTaskByName(taskName: String): Task?

    @Query("select * from task")
    fun getAllTasks(): Flow<List<Task>>

    @Insert
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Transaction
    @Query("select * from task where name = :taskName")
    fun getSessionsByTaskStream(taskName: String): Flow<TaskWithSessions?>

    @Transaction
    @Query("select * from task where name = :taskName")
    suspend fun getSessionsByTask(taskName: String): TaskWithSessions?
}