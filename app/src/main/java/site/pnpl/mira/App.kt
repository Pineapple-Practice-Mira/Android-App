package site.pnpl.mira

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import site.pnpl.mira.di.AppComponent
import site.pnpl.mira.di.DaggerAppComponent
import site.pnpl.mira.di.modules.DatabaseModule
import site.pnpl.mira.di.modules.DomainModule
import site.pnpl.mira.di.modules.RemoteModule
import site.pnpl.mira.di.modules.RepositoryModule

class App : Application() {

    lateinit var appComponent: AppComponent
    private val applicationScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        instance = this

        appComponent = DaggerAppComponent.builder()
            .domainModule(DomainModule(this, applicationScope))
            .databaseModule(DatabaseModule())
            .repositoryModule(RepositoryModule())
            .remoteModule(RemoteModule())
            .build()
    }

    override fun onTerminate() {
        super.onTerminate()
        applicationScope.coroutineContext.cancelChildren()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}