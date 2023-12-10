package site.pnpl.mira.ui.statistic.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeBounds
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.SelectedPeriod
import site.pnpl.mira.databinding.FragmentStatisticsByFactorBinding
import site.pnpl.mira.model.FactorData
import site.pnpl.mira.ui.home.customview.ActionBar
import site.pnpl.mira.ui.statistic.fragments.StatisticsFragment.Companion.KEY_FACTOR_DATA
import site.pnpl.mira.ui.statistic.fragments.StatisticsFragment.Companion.KEY_TRANSITION
import site.pnpl.mira.ui.statistic.fragments.StatisticsFragment.Companion.KEY_TRANSITION_STATISTIC_BY_FACTOR
import javax.inject.Inject

class StatisticsByFactorFragment : Fragment(R.layout.fragment_statistics_by_factor) {
    private var _binding: FragmentStatisticsByFactorBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var selectedPeriod: SelectedPeriod
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStatisticsByFactorBinding.inflate(layoutInflater, container, false)

        sharedElementEnterTransition = ChangeBounds().apply {
            duration = StatisticsFragment.ANIMATION_TRANSITION_DURATION
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = StatisticsFragment.ANIMATION_TRANSITION_DURATION
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

//        binding.checkIn.setOnClickListener {
//            findNavController().navigate(R.id.action_stat_by_factor_to_details)
//        }

        initActionBar()
        setClickListeners()
        createFactorAnalysisView()
        initText()
        binding.factorAnalysisView.startAnimation(1)
    }

    private fun initActionBar() {
        with(binding.actionBar) {
            initDatePicker(selectedPeriod.startPeriod, selectedPeriod.endPeriod)
            setDisplayMode(ActionBar.DisplayMode.STATISTIC_SECOND)
        }
    }


    private fun initText() {
        val text = " ${getString(R.string.statistic_factor_name, binding.factorAnalysisView.factorName)}"
        binding.factorName.apply {
            this.text = text
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(ANIMATION_DURATION_ALPHA_TEXT)
                .start()
        }
    }

    private fun setClickListeners() {
        binding.back.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.title to "title"
            )
            findNavController().navigate(
                R.id.action_stat_by_factor_to_statistic,
                bundleOf(
                    Pair(KEY_TRANSITION, KEY_TRANSITION_STATISTIC_BY_FACTOR)
                ),
                null,
                extras
            )
        }
    }

    private fun createFactorAnalysisView() {
        val factorData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            findNavController().currentBackStackEntry?.arguments?.getParcelable(KEY_FACTOR_DATA, FactorData::class.java)
        } else {
            @Suppress("DEPRECATION")
            findNavController().currentBackStackEntry?.arguments?.getParcelable(KEY_FACTOR_DATA)
        }

        factorData?.let {
            binding.factorAnalysisView.apply {
                init(
                    it.positiveCount,
                    it.negativeCount,
                    getString(it.nameIdRes)
                )
                startAnimation(1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ANIMATION_DURATION_ALPHA_TEXT = 300L
    }
}