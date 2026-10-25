package me.linhvo.focus.database.dao

import androidx.room3.Dao
import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.ForeignKey.Companion.CASCADE
import androidx.room3.Index
import androidx.room3.Insert
import androidx.room3.PrimaryKey
import androidx.room3.Query
import androidx.room3.Relation
import androidx.room3.Transaction
import androidx.room3.Update
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Entity(
    indices = [Index(value = ["taskName"])],
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["name"],
            childColumns = ["taskName"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
@Serializable
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val startTime: Instant,
    val stopTime: Instant,
    val taskName: String
)

data class TaskWithSessions(
    @Embedded val task: Task,
    @Relation(parentColumns = ["name"], entityColumns = ["taskName"])
    val sessions: List<Session>
)

@Dao
interface SessionDao {
    @Insert
    suspend fun addSession(session: Session)

    @Update
    suspend fun updateSession(session: Session)

    @Query("delete from session where id = :id")
    suspend fun deleteSession(id: Long)

    @Transaction
    @Query("select * from task where name = :taskName")
    suspend fun getSessionsByTask(taskName: String): TaskWithSessions
}