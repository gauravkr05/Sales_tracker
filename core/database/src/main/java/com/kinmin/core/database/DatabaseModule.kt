package com.kinmin.core.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): KinminDatabase =
        Room.databaseBuilder(ctx, KinminDatabase::class.java, "kinmin.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun orderDao(db: KinminDatabase) = db.orderDao()
    @Provides fun activityDao(db: KinminDatabase) = db.activityDao()
    @Provides fun remarkDao(db: KinminDatabase) = db.remarkDao()
    @Provides fun locationDao(db: KinminDatabase) = db.locationDao()
}
