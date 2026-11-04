package me.linhvo.focus404.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.linhvo.focus404.database.LocalDatabaseV1
import me.linhvo.focus404.database.dao.SessionDao
import me.linhvo.focus404.database.dao.TaskDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun provideTaskDao(database: LocalDatabaseV1): TaskDao = database.taskDao()

    @Provides
    fun provideSessionDao(database: LocalDatabaseV1): SessionDao = database.sessionDao()
}