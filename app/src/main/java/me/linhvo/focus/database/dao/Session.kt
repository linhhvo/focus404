package me.linhvo.focus.database.dao

import androidx.room3.*
import androidx.room3.ForeignKey.Companion.CASCADE
import kotlinx.coroutines.flow.Flow
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
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Instant,
    val stopTime: Instant? = null,
    val taskName: String
)

@Dao
interface SessionDao {
    @Query("select * from session where id = :id")
    suspend fun getSessionById(id: Long): Session?

    @Query("select * from session")
    fun observeAllSessions(): Flow<List<Session>>

    @Insert
    suspend fun addSession(session: Session): Long

    @Update
    suspend fun updateSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)
}