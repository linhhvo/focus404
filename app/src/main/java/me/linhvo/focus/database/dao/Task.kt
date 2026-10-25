package me.linhvo.focus.database.dao

import androidx.room3.Dao
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.Insert
import androidx.room3.PrimaryKey
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Entity
@Serializable
data class Task(
    @PrimaryKey val name: String,
)

@Dao
interface TaskDao {
    @Query("select * from task where name = :taskName")
    suspend fun getTask(taskName: String): Task?

    @Query("select * from task")
    suspend fun getAllTasks(): List<Task>

    @Insert
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("delete from task where name = :taskName")
    suspend fun deleteTask(taskName: String)
}