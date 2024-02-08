package site.pnpl.mira.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import site.pnpl.mira.data.repositories.EmotionRepository
import site.pnpl.mira.domain.EmotionCreator
import site.pnpl.mira.domain.EmotionProvider
import site.pnpl.mira.domain.SelectedPeriod
import site.pnpl.mira.domain.SettingsProvider
import site.pnpl.mira.domain.SettingsProviderImpl
import site.pnpl.mira.utils.OFFSET_DAYS_FOR_DEFAULT_PERIOD
import java.util.Calendar
import javax.inject.Singleton

@Module
class DomainModule(val context: Context, val applicationScope: CoroutineScope) {

    @Provides
    fun provideContext() = context

    @Provides
    fun provideApplicationScope() = applicationScope

    @Provides
    fun provideSettingsProvider(context: Context): SettingsProvider = SettingsProviderImpl(context)

    @Singleton
    @Provides
    fun provideSelectedPeriod(settingsProvider: SettingsProvider): SelectedPeriod {
        val endPeriod = System.currentTimeMillis()

        val firstDayMonthInFirstStart = Calendar.getInstance().apply {
            timeInMillis = settingsProvider.getFirstStartMonth()
            set(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis

        val endDateWithOffset = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.DAY_OF_YEAR, OFFSET_DAYS_FOR_DEFAULT_PERIOD)
        }.timeInMillis

        val startPeriod = if (endDateWithOffset < firstDayMonthInFirstStart) {
            firstDayMonthInFirstStart
        } else {
            endDateWithOffset
        }

        return SelectedPeriod(startPeriod, endPeriod)
    }

    @Singleton
    @Provides
    fun provideEmotionCreator(
        context: Context,
        repository: EmotionRepository,
        applicationScope: CoroutineScope,
        emotionProvider: EmotionProvider
    ): EmotionCreator = EmotionCreator(context, repository, applicationScope, emotionProvider)

    @Singleton
    @Provides
    fun provideEmotionProvider(
        repository: EmotionRepository,
        applicationScope: CoroutineScope
    ): EmotionProvider = EmotionProvider(repository, applicationScope)
}