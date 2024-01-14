package site.pnpl.mira.di.modules

import dagger.Module
import dagger.Provides
import site.pnpl.mira.data.CheckInRepository
import site.pnpl.mira.data.EmotionRepository
import site.pnpl.mira.data.database.check_in.dao.CheckInDao
import site.pnpl.mira.data.database.emotions.EmotionDao
import site.pnpl.mira.data.remote.MiraApi
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideCheckInRepository(checkInDao: CheckInDao) = CheckInRepository(checkInDao)

    @Singleton
    @Provides
    fun provideEmotionRepository(retrofitService: MiraApi, emotionDao: EmotionDao) = EmotionRepository(retrofitService, emotionDao)
}