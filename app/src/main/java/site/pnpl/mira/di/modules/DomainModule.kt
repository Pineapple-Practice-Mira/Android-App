package site.pnpl.mira.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import site.pnpl.mira.data.SelectedPeriod
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.data.SettingsProviderImpl
import site.pnpl.mira.utils.OFFSET_DAYS_FOR_DEFAULT_PERIOD
import java.util.Calendar
import javax.inject.Singleton

@Module
class DomainModule(val context: Context) {

    @Provides
    fun provideContext() = context

    @Provides
    fun provideSettingsProvider(context: Context): SettingsProvider = SettingsProviderImpl(context)

    @Singleton
    @Provides
    fun provideSelectedPeriod(): SelectedPeriod {
        val endPeriod = System.currentTimeMillis()
        var startPeriod: Long
        Calendar.getInstance().apply {
            timeInMillis = endPeriod
            add(Calendar.DAY_OF_YEAR, OFFSET_DAYS_FOR_DEFAULT_PERIOD)
            startPeriod = timeInMillis
        }
        return SelectedPeriod(startPeriod, endPeriod)
    }
}