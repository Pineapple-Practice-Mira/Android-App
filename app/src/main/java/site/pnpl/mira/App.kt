package site.pnpl.mira

import android.app.Application
import site.pnpl.mira.di.AppComponent
import site.pnpl.mira.di.DaggerAppComponent
import site.pnpl.mira.di.modules.DomainModule
import site.pnpl.mira.ui.home.recycler_view.CheckInItem

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

        val ITEMS = mutableListOf<CheckInItem>()
    }
}