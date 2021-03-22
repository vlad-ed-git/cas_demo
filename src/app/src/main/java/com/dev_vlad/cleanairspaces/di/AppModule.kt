package com.dev_vlad.cleanairspaces.di

import com.dev_vlad.cleanairspaces.models.repository.LocationsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun provideLocationsRepo(): LocationsRepo = LocationsRepo()
}