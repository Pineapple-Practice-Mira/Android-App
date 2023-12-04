package site.pnpl.mira.di

import dagger.Component
import site.pnpl.mira.di.modules.DatabaseModule
import site.pnpl.mira.di.modules.DomainModule
import site.pnpl.mira.ui.check_in.CheckInDetailsViewModel
import site.pnpl.mira.ui.check_in.CheckInViewModel
import site.pnpl.mira.ui.exercise.ExercisesListViewModel
import site.pnpl.mira.ui.greeting.fragments.AcquaintanceFragment
import site.pnpl.mira.ui.greeting.fragments.GreetingFragment
import site.pnpl.mira.ui.greeting.fragments.InterNameFragment
import site.pnpl.mira.ui.greeting.fragments.SplashFragment
import site.pnpl.mira.ui.home.fragments.HomeFragment
import site.pnpl.mira.ui.home.HomeViewModel
import site.pnpl.mira.ui.home.fragments.SettingsFragment
import site.pnpl.mira.ui.statistic.StatisticViewModel
import site.pnpl.mira.ui.statistic.fragments.StatisticsFragment
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
    fun inject(homeFragment: HomeFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(checkInDetailsViewModel: CheckInDetailsViewModel)
    fun inject(statisticViewModel: StatisticViewModel)
    fun inject(statisticsFragment: StatisticsFragment)

}