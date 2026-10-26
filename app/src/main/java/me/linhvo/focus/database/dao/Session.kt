package me.linhvo.focus.database.dao

import androidx.room3.Dao
import androidx.room3.Delete
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
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Instant,
    val stopTime: Instant? = null,
    val taskName: String
)

@Dao
interface SessionDao {
    @Query("select * from session")
    suspend fun getAllSessions(): List<Session>

    @Insert
    suspend fun addSession(session: Session): Long

    @Update
    suspend fun updateSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)
}