package com.kinmin.feature.remarks.di

import com.kinmin.feature.remarks.data.RemarkRepositoryImpl
import com.kinmin.feature.remarks.domain.RemarkRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemarkModule {
    @Binds @Singleton
    abstract fun bind(impl: RemarkRepositoryImpl): RemarkRepository
}
