package com.applabs.eventx.di

import android.app.Application
import androidx.room.Room
import com.applabs.eventx.events.data.local.event.EventDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Utsav Devadiga
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesMovieDatabase(app: Application): EventDatabase {
        return Room.databaseBuilder(
            app,
            EventDatabase::class.java,
            "moviedb.db"
        ).build()
    }
}