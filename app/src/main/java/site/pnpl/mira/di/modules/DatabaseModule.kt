package site.pnpl.mira.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import site.pnpl.mira.data.CheckInRepository
import site.pnpl.mira.data.DBConstants
import site.pnpl.mira.data.MiraDatabase
import site.pnpl.mira.data.dao.CheckInDao
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
    fun provideCheckInRepository(checkInDao: CheckInDao) = CheckInRepository(checkInDao)

}