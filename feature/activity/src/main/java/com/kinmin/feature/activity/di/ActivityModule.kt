package com.kinmin.feature.activity.di

import com.kinmin.feature.activity.data.ActivityRepositoryImpl
import com.kinmin.feature.activity.domain.ActivityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ActivityModule {
    @Binds @Singleton
    abstract fun bind(impl: ActivityRepositoryImpl): ActivityRepository
}
