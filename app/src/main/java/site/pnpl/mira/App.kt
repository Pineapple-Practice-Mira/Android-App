package site.pnpl.mira

import android.app.Application
import site.pnpl.mira.di.AppComponent
import site.pnpl.mira.di.DaggerAppComponent
import site.pnpl.mira.di.modules.DomainModule

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        appComponent = DaggerAppComponent.builder()
            .domainModule(DomainModule(this))
            .build()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}