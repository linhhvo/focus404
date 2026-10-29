package me.linhvo.focus.database.di

import android.content.Context
import androidx.room3.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.linhvo.focus.database.LocalDatabaseV1
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): LocalDatabaseV1 =
        Room.databaseBuilder(
            context = context,
            klass = LocalDatabaseV1::class.java,
            name = "focus-db"
        ).build()
}