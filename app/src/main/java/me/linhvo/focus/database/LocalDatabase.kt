package me.linhvo.focus.database

import androidx.room3.ColumnTypeConverter
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import me.linhvo.focus.database.dao.Session
import me.linhvo.focus.database.dao.SessionDao
import me.linhvo.focus.database.dao.Task
import me.linhvo.focus.database.dao.TaskDao
import kotlin.time.Instant

@Database(
    entities = [
        Task::class,
        Session::class
    ],
    version = 1,
    exportSchema = false
)

@ColumnTypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun sessionDao(): SessionDao
}

internal class Converters {
    @ColumnTypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @ColumnTypeConverter
    fun instantToLong(value: Instant?): Long? =
        value?.toEpochMilliseconds()
}