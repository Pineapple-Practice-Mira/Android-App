package site.pnpl.mira.di

import dagger.Component
import site.pnpl.mira.di.modules.DomainModule
import site.pnpl.mira.ui.greeting.fragments.AcquaintanceFragment
import site.pnpl.mira.ui.greeting.fragments.InterNameFragment
import site.pnpl.mira.ui.greeting.fragments.SplashFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModule::class])
interface AppComponent {
    fun inject(splashFragment: SplashFragment)
    fun inject(splashFragment: InterNameFragment)
    fun inject(acquaintanceFragment: AcquaintanceFragment)

}