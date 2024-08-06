package com.applabs.eventx.di

import com.applabs.eventx.events.data.repository.EventListRepositoryImpl
import com.applabs.eventx.events.domain.repository.EventListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Utsav Devadiga
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun bindEventListRepository(
        eventListRepositoryImpl: EventListRepositoryImpl
    ): EventListRepository


}