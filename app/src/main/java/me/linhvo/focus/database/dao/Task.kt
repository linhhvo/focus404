package me.linhvo.focus.database.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.Insert
import androidx.room3.PrimaryKey
import androidx.room3.Query
import androidx.room3.Relation
import androidx.room3.Transaction
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Entity(indices = [Index(value = ["name"], unique = true)])
@Serializable
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val totalFocusDuration: Duration = Duration.ZERO
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
    suspend fun getSessionsByTask(taskName: String): TaskWithSessions
}