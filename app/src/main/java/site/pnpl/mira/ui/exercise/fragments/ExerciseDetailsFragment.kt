package site.pnpl.mira.ui.exercise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentExerciseDetailsBinding
import site.pnpl.mira.domain.analitycs.Analytics
import site.pnpl.mira.domain.analitycs.AnalyticsEvent
import site.pnpl.mira.domain.analitycs.EventParameter
import site.pnpl.mira.models.ScreenUI
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment
import site.pnpl.mira.ui.exercise.viewpager.ExerciseVPAdapter
import site.pnpl.mira.ui.extensions.getParcelableArrayListCompat
import site.pnpl.mira.ui.greeting.fragments.GreetingFragment.Companion.SCREENS_KEY
import java.util.ArrayList
import javax.inject.Inject

class ExerciseDetailsFragment : Fragment(R.layout.fragment_exercise_details) {
    private var _binding: FragmentExerciseDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ExerciseVPAdapter
    private lateinit var viewPager: ViewPager2

    @Inject lateinit var analytics: Analytics
    private var callbackKey: String? = null
    private var screens: ArrayList<ScreenUI>? = null
    private var maxPosition = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExerciseDetailsBinding.bind(view)
        App.instance.appComponent.inject(this)
        callbackKey = findNavController().currentBackStackEntry?.arguments?.getString(CheckInSavedFragment.CALLBACK_KEY)

        screens = arguments?.getParcelableArrayListCompat(SCREENS_KEY)

        screens?.sortBy { it.sequenceNumber }

        initViewPager()
        setViewPagerListener()
        setOnClickListeners()
    }

    private fun initViewPager() {
        adapter = ExerciseVPAdapter(this, screens!!)
        viewPager = binding.viewPager
        viewPager.adapter = adapter

        binding.indicator.setViewPager(viewPager)
        adapter.registerAdapterDataObserver(binding.indicator.adapterDataObserver)
    }

    private fun setViewPagerListener() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position > maxPosition) {
                    analytics.sendEvent(
                        AnalyticsEvent.NAME_EXERCISE_PROGRESS,
                        listOf(EventParameter(AnalyticsEvent.PARAMETER_STEP, position))
                    )
                    maxPosition = position
                }

                with(binding) {
                    btnPrevious.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
                    btnNext.visibility = if (position == screens!!.size - 1) View.INVISIBLE else View.VISIBLE
                    btnSkip2.visibility = if (position == screens!!.size - 1) View.VISIBLE else View.INVISIBLE
                }
            }
        })
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnNext.setOnClickListener {
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
            }

            btnPrevious.setOnClickListener {
                viewPager.setCurrentItem(viewPager.currentItem - 1, true)
            }

            close.setOnClickListener {
                if (viewPager.currentItem == screens!!.size - 1) {
                    analytics.sendEvent(AnalyticsEvent.NAME_EXERCISE_CLOSE)
                } else {
                    analytics.sendEvent(
                        AnalyticsEvent.NAME_EXERCISE_PROGRESS,
                        listOf(EventParameter(AnalyticsEvent.PARAMETER_SKIP, viewPager.currentItem)))
                }
                navigateToBackStack()
            }

            btnSkip2.setOnClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_EXERCISE_CLOSE)
                navigateToBackStack()
            }
        }
    }

    private fun navigateToBackStack() {
        when (callbackKey) {
            CheckInSavedFragment.CALLBACK_HOME -> findNavController().navigate(R.id.action_exercise_details_fragment_to_home)
            else -> findNavController().navigate(R.id.action_exercise_details_fragment_to_exercise_list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}