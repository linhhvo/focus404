package me.linhvo.focus.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.linhvo.focus.database.LocalDatabase
import me.linhvo.focus.database.dao.SessionDao
import me.linhvo.focus.database.dao.TaskDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun provideTaskDao(database: LocalDatabase): TaskDao = database.taskDao()

    @Provides
    fun provideSessionDao(database: LocalDatabase): SessionDao = database.sessionDao()
}