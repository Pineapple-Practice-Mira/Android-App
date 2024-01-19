package site.pnpl.mira.di

import dagger.Component
import site.pnpl.mira.di.modules.AnalyticsModule
import site.pnpl.mira.di.modules.DatabaseModule
import site.pnpl.mira.di.modules.DomainModule
import site.pnpl.mira.di.modules.RemoteModule
import site.pnpl.mira.di.modules.RepositoryModule
import site.pnpl.mira.ui.MainActivity
import site.pnpl.mira.ui.check_in.CheckInDetailsViewModel
import site.pnpl.mira.ui.check_in.CheckInSavedViewModel
import site.pnpl.mira.ui.check_in.CheckInViewModel
import site.pnpl.mira.ui.check_in.fragments.CheckInDetailsItemFragment
import site.pnpl.mira.ui.check_in.fragments.CheckInFeelFragment
import site.pnpl.mira.ui.check_in.fragments.CheckInFragment
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment
import site.pnpl.mira.ui.customview.ActionBar
import site.pnpl.mira.ui.customview.BottomBar
import site.pnpl.mira.ui.exercise.fragments.ExercisePreviewFragment
import site.pnpl.mira.ui.exercise.fragments.ExercisesListFragment
import site.pnpl.mira.ui.exercise.ExercisesListViewModel
import site.pnpl.mira.ui.exercise.fragments.ExerciseDetailsFragment
import site.pnpl.mira.ui.greeting.GreetingViewModel
import site.pnpl.mira.ui.greeting.fragments.AcquaintanceFragment
import site.pnpl.mira.ui.greeting.fragments.GreetingFragment
import site.pnpl.mira.ui.greeting.fragments.InterNameFragment
import site.pnpl.mira.ui.greeting.fragments.SplashFragment
import site.pnpl.mira.ui.home.fragments.HomeFragment
import site.pnpl.mira.ui.home.HomeViewModel
import site.pnpl.mira.ui.home.fragments.SettingsFragment
import site.pnpl.mira.ui.home.recycler_view.CheckInAdapter
import site.pnpl.mira.ui.statistic.StatisticByFactorViewModel
import site.pnpl.mira.ui.statistic.StatisticViewModel
import site.pnpl.mira.ui.statistic.fragments.StatisticsByFactorFragment
import site.pnpl.mira.ui.statistic.fragments.StatisticsFragment
import site.pnpl.mira.ui.statistic.recycler_view.CheckInStatisticAdapter
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DomainModule::class,
        DatabaseModule::class,
        RemoteModule::class,
        RepositoryModule::class,
        AnalyticsModule::class
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
    fun inject(statisticsByFactorFragment: StatisticsByFactorFragment)
    fun inject(statisticByFactorViewModel: StatisticByFactorViewModel)
    fun inject(mainActivity: MainActivity)
    fun inject(checkInFeelFragment: CheckInFeelFragment)
    fun inject(checkInSavedFragment: CheckInSavedFragment)
    fun inject(checkInDetailsItemFragment: CheckInDetailsItemFragment)
    fun inject(checkInAdapter: CheckInAdapter)
    fun inject(checkInStatisticAdapter: CheckInStatisticAdapter)
    fun inject(greetingViewModel: GreetingViewModel)
    fun inject(exercisesListFragment: ExercisesListFragment)
    fun inject(exerciseFragment: ExercisePreviewFragment)
    fun inject(exerciseDetailsFragment: ExerciseDetailsFragment)
    fun inject(checkInSavedViewModel: CheckInSavedViewModel)
    fun inject(checkInFragment: CheckInFragment)
    fun inject(actionBar: ActionBar)
    fun inject(bottomBar: BottomBar)

}