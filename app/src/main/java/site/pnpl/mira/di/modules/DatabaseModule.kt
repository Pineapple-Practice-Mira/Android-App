package site.pnpl.mira.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import site.pnpl.mira.data.database.DBConstants
import site.pnpl.mira.data.database.MiraDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideCheckInDao(context: Context) =
        Room.databaseBuilder(
            context,
            MiraDatabase::class.java,
            DBConstants.NAME
        ).build().checkInDao()

    @Singleton
    @Provides
    fun provideEmotionDao(context: Context) =
        Room.databaseBuilder(
            context,
            MiraDatabase::class.java,
            DBConstants.NAME
        ).build().emotionDao()

    @Singleton
    @Provides
    fun provideExerciseDao(context: Context) =
        Room.databaseBuilder(
            context,
            MiraDatabase::class.java,
            DBConstants.NAME
        ).build().exerciseDao()

}