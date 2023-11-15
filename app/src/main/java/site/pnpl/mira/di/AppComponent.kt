package site.pnpl.mira.di

import dagger.Component
import site.pnpl.mira.di.modules.DatabaseModule
import site.pnpl.mira.di.modules.DomainModule
import site.pnpl.mira.ui.check_in.CheckInViewModel
import site.pnpl.mira.ui.exercise.ExercisesListViewModel
import site.pnpl.mira.ui.greeting.fragments.AcquaintanceFragment
import site.pnpl.mira.ui.greeting.fragments.GreetingFragment
import site.pnpl.mira.ui.greeting.fragments.InterNameFragment
import site.pnpl.mira.ui.greeting.fragments.SplashFragment
import site.pnpl.mira.ui.home.HomeViewModel
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DomainModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {
    fun inject(splashFragment: SplashFragment)
    fun inject(splashFragment: InterNameFragment)
    fun inject(acquaintanceFragment: AcquaintanceFragment)
    fun inject(greetingFragment: GreetingFragment)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(exercisesListViewModel: ExercisesListViewModel)
    fun inject(checkInViewModel: CheckInViewModel)

}