package site.pnpl.mira.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import site.pnpl.mira.domain.analitycs.Analytics
import site.pnpl.mira.domain.analitycs.MixPanelAnalytics
import javax.inject.Singleton

@Module
class AnalyticsModule {
    @Singleton
    @Provides
    fun provideAnalytics(applicationContext: Context): Analytics = MixPanelAnalytics(applicationContext)
}