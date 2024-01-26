package site.pnpl.mira.ui.greeting.fragments

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import site.pnpl.mira.App
import site.pnpl.mira.ui.greeting.viewpager.VpAdapter
import site.pnpl.mira.R
import site.pnpl.mira.domain.SettingsProvider
import site.pnpl.mira.databinding.FragmentAcquaintanceBinding
import site.pnpl.mira.domain.analitycs.Analytics
import site.pnpl.mira.domain.analitycs.AnalyticsEvent
import site.pnpl.mira.domain.analitycs.EventParameter
import site.pnpl.mira.models.ScreenUI
import site.pnpl.mira.ui.extensions.getParcelableArrayListCompat
import site.pnpl.mira.ui.greeting.fragments.GreetingFragment.Companion.SCREENS_KEY
import java.util.ArrayList
import javax.inject.Inject

class AcquaintanceFragment : Fragment(R.layout.fragment_acquaintance) {
    private var _binding: FragmentAcquaintanceBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VpAdapter
    private lateinit var viewPager: ViewPager2

    @Inject lateinit var settingsProvider: SettingsProvider
    @Inject lateinit var analytics: Analytics
    private var screens: ArrayList<ScreenUI>? = null
    private var maxPosition = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAcquaintanceBinding.bind(view)
        App.instance.appComponent.inject(this)

        screens = arguments?.getParcelableArrayListCompat(SCREENS_KEY)

        screens!!.sortBy { it.sequenceNumber }

        initViewPager()
        setViewPagerListener()
        setOnClickListeners()
    }

    private fun initViewPager() {
        adapter = VpAdapter(requireActivity(), screens!!)
        viewPager = binding.viewPager
        viewPager.adapter = adapter

        binding.indicator.setViewPager(viewPager)
        adapter.registerAdapterDataObserver(binding.indicator.adapterDataObserver)
    }

    private fun setViewPagerListener() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                if (position > maxPosition) {
                    analytics.sendEvent(
                        AnalyticsEvent.NAME_GREETING_PROGRESS,
                        listOf(EventParameter(AnalyticsEvent.PARAMETER_STEP, position))
                    )
                    maxPosition = position
                }

                super.onPageSelected(position)
                with(binding) {
                    btnPrevious.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
                    btnNext.visibility = if (position == screens!!.size - 1) View.INVISIBLE else View.VISIBLE
                    btnSkip2.visibility = if (position == screens!!.size - 1) View.VISIBLE else View.INVISIBLE

                    btnSkip.text = if (position == screens!!.size - 1) resources.getString(R.string.complete) else resources.getString(R.string.skip)
                    btnSkip.paintFlags = Paint.UNDERLINE_TEXT_FLAG

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

            btnSkip.setOnClickListener {
                if (viewPager.currentItem == screens!!.size - 1) {
                    analytics.sendEvent(AnalyticsEvent.NAME_GREETING_COMPLETE_VIA_BUTTON)
                } else {
                    analytics.sendEvent(
                        AnalyticsEvent.NAME_GREETING_PROGRESS,
                        listOf(EventParameter(AnalyticsEvent.PARAMETER_SKIP, viewPager.currentItem)))
                }
                navigateToHome()
            }

            btnSkip2.setOnClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_GREETING_COMPLETE)
                navigateToHome()
            }
        }
    }

    private fun navigateToHome() {
        settingsProvider.firstLaunchCompleted()
        findNavController().navigate(R.id.action_acquaintance_to_home)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}