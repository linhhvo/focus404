package me.linhvo.focus404.database

import androidx.room3.ColumnTypeConverter
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import me.linhvo.focus404.database.dao.Session
import me.linhvo.focus404.database.dao.SessionDao
import me.linhvo.focus404.database.dao.Task
import me.linhvo.focus404.database.dao.TaskDao
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.Instant
import kotlin.time.toDuration

@Database(
    entities = [
        Task::class,
        Session::class
    ],
    version = 1,
    exportSchema = true
)

@ColumnTypeConverters(Converters::class)
abstract class LocalDatabaseV1 : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun sessionDao(): SessionDao
}

//@Database(
//    entities = [
//        Task::class,
//        Session::class
//    ],
//    version = 2,
//    autoMigrations = [AutoMigration(from = 1, to = 2)],
//    exportSchema = true
//)

//@ColumnTypeConverters(Converters::class)
//abstract class LocalDatabaseV2 : RoomDatabase() {
//    abstract fun taskDao(): TaskDao
//    abstract fun sessionDao(): SessionDao
//}

internal class Converters {
    @ColumnTypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @ColumnTypeConverter
    fun instantToLong(value: Instant?): Long? =
        value?.toEpochMilliseconds()

    @ColumnTypeConverter
    fun durationToLong(value: Duration?): Long? =
        value?.toLong(DurationUnit.MILLISECONDS)

    @ColumnTypeConverter
    fun longToDuration(value: Long?): Duration? =
        value?.toDuration(DurationUnit.MILLISECONDS)
}