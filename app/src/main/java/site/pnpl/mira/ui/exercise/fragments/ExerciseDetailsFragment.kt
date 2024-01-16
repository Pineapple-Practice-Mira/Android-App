package site.pnpl.mira.ui.exercise.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import site.pnpl.mira.App
import site.pnpl.mira.ui.greeting.viewpager.VpAdapter
import site.pnpl.mira.R
import site.pnpl.mira.domain.SettingsProvider
import site.pnpl.mira.databinding.FragmentExerciseDetailsBinding
import site.pnpl.mira.models.ScreenUI
import site.pnpl.mira.ui.greeting.fragments.GreetingFragment.Companion.SCREENS_KEY
import java.util.ArrayList
import javax.inject.Inject

class ExerciseDetailsFragment : Fragment(R.layout.fragment_exercise_details) {
    private var _binding: FragmentExerciseDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VpAdapter
    private lateinit var viewPager: ViewPager2

    @Inject
    lateinit var settingsProvider: SettingsProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExerciseDetailsBinding.bind(view)
        App.instance.appComponent.inject(this)

        val screens = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(SCREENS_KEY, ScreenUI::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelableArrayList(SCREENS_KEY)
        }

        screens!!.sortBy { it.sequenceNumber }

        initViewPager(screens)
        setViewPagerListener(screens)
        setOnClickListeners()
    }

    private fun initViewPager(screens: ArrayList<ScreenUI>) {
        adapter = VpAdapter(requireActivity(), screens)
        viewPager = binding.viewPager
        viewPager.adapter = adapter

        binding.indicator.setViewPager(viewPager)
        adapter.registerAdapterDataObserver(binding.indicator.adapterDataObserver)
    }

    private fun setViewPagerListener(screens: ArrayList<ScreenUI>) {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                with(binding) {
                    btnPrevious.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
                    btnNext.visibility = if (position == screens.size - 1) View.INVISIBLE else View.VISIBLE
                    btnSkip2.visibility = if (position == screens.size - 1) View.VISIBLE else View.INVISIBLE
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
                findNavController().popBackStack()
            }

            btnSkip2.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}