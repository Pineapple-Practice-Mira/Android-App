package site.pnpl.mira.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.data.SettingsProviderImpl

@Module
class DomainModule(val context: Context) {

    @Provides
    fun provideContext() = context

    @Provides
    fun provideSettingsProvider(context: Context) : SettingsProvider = SettingsProviderImpl(context)
}