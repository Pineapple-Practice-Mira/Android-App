package site.pnpl.mira.ui.statistic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.ChangeBounds
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.SelectedPeriod
import site.pnpl.mira.databinding.FragmentStatisticsBinding
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.model.Emotion
import site.pnpl.mira.model.EmotionsList
import site.pnpl.mira.model.FactorData
import site.pnpl.mira.model.FactorsList
import site.pnpl.mira.ui.home.customview.ActionBar
import site.pnpl.mira.ui.statistic.StatisticViewModel
import site.pnpl.mira.ui.statistic.customview.FactorAnalysisView
import javax.inject.Inject

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StatisticViewModel by viewModels()

    @Inject lateinit var selectedPeriod: SelectedPeriod

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = ANIMATION_TRANSITION_DURATION
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = ANIMATION_TRANSITION_DURATION
        }
        _binding = FragmentStatisticsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        App.instance.appComponent.inject(this)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.dark_grey)

        animationBody()
        initActionBar()
        setClickListener()
        getCheckIns()
        viewModelListener()
    }

    private fun animationBody() {
        val key = findNavController().currentBackStackEntry?.arguments?.getString(KEY_TRANSITION)
        key?.let {
            if (key == KEY_TRANSITION_HOME) {
                val animationUp = AnimationUtils.loadAnimation(requireContext(), R.anim.check_in_up)
                binding.mainContainer.startAnimation(animationUp)
            }
        }
    }

    private fun initActionBar() {
        with(binding.actionBar) {
            //Выбранный период для календаря
            initDatePicker(selectedPeriod.startPeriod, selectedPeriod.endPeriod)
            setDisplayMode(ActionBar.DisplayMode.STATISTIC_FIRST)
            setCalendarPeriodSelectionListener(childFragmentManager) { period ->
                selectedPeriod.startPeriod = period.first
                selectedPeriod.endPeriod = period.second
                getCheckIns()
            }
        }
    }

    private fun setClickListener() {
        binding.close.setOnClickListener {
            val animationDown = AnimationUtils.loadAnimation(requireContext(), R.anim.check_in_down)
            binding.mainContainer.startAnimation(animationDown)
            findNavController().popBackStack()
        }
    }

    private fun getCheckIns() {
        viewModel.getCheckInForPeriod(selectedPeriod.startPeriod, selectedPeriod.endPeriod)
    }

    private fun viewModelListener() {
        viewModel.onGetEvent().observe(viewLifecycleOwner) { event ->
            val checkIns = event.contentIfNotHandled
            if (checkIns != null) {
                fillFactorAnalysisViews(checkIns)
            }
        }
    }

    private fun fillFactorAnalysisViews(checkIns: List<CheckInUI>) {
        binding.factorAnalysisContainer.removeAllViews()

        if (checkIns.isEmpty()) {
            enableNotice(true)
            return
        } else {
            enableNotice(false)
        }

        val factors = mutableListOf<FactorData>()
        checkIns.forEach { checkIn ->
            var factor = factors.find { it.id == checkIn.factorId }
            if (factor == null) {
                factors.add(
                    FactorData(
                        checkIn.factorId,
                        FactorsList.factors[checkIn.factorId].nameResId
                    )
                )
                factor = factors[factors.size - 1]
            }
            factor.apply {
                when (EmotionsList.emotions[checkIn.emotionId].type) {
                    Emotion.Type.POSITIVE -> positiveCount++
                    Emotion.Type.NEGATIVE -> negativeCount++
                }
                totalCount++
            }
        }

        factors.sortByDescending { it.totalCount }
        factors.forEach { factorData ->
            val factorAnalysisView = FactorAnalysisView(
                requireContext(),
                factorName = getString(factorData.nameIdRes),
                positiveCount = factorData.positiveCount,
                negativeCount = factorData.negativeCount
            ).apply {
                setOnClickListener {
                    this.z = 2f
                    factorAnalysisOnClick(this, factorData)
                }
            }

            binding.factorAnalysisContainer.addView(factorAnalysisView)
            factorAnalysisView.startAnimation()
        }
        binding.factorAnalysisContainer.startLayoutAnimation()
    }

    private fun factorAnalysisOnClick(fav: FactorAnalysisView, factorData: FactorData) {

//        val extrasBuilder = FragmentNavigator.Extras.Builder()
//
//        binding.factorAnalysisContainer.children.forEach { fav ->
//            fav.transitionName = FAV_TRANSITION_NAME
//            extrasBuilder.addSharedElement(fav, "factorAnalysisView")
//        }
//
//        extrasBuilder.addSharedElement(binding.actionBar, "statisticByFactorActionBar")
//        extrasBuilder.addSharedElement(binding.title,"title")
//        val extras = extrasBuilder.build()

        fav.transitionName = FAV_TRANSITION_NAME
        val extras = FragmentNavigatorExtras(
            fav to "factorAnalysisView",
            binding.actionBar to "statisticByFactorActionBar",
            binding.title to "title"
        )

        findNavController().navigate(
            R.id.action_statistics_to_statistics_by_factor,
            bundleOf(
                Pair(KEY_FACTOR_DATA, factorData)
            ),
            null,
            extras
        )
    }

    //        val extras = FragmentNavigatorExtras(
//            binding.actionBar to "statisticByFactorActionBar",
//            binding.factorAnalysisContainer to "factorAnalysisView",
//            binding.title to "title"
//        )

    private fun enableNotice(value: Boolean) {
        with(binding) {
            emotionNotice.isVisible = value
            textViewNotice.isVisible = value
            textViewInfo.isVisible = !value
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ANIMATION_TRANSITION_DURATION = 300L
        const val KEY_FACTOR_DATA = "FACTOR_DATA"
        const val FAV_TRANSITION_NAME = "FactorAnalysisView"

        const val KEY_TRANSITION = "KEY_TRANSITION"
        const val KEY_TRANSITION_HOME = "KEY_TRANSITION_HOME"
        const val KEY_TRANSITION_STATISTIC_BY_FACTOR = "KEY_TRANSITION_STATISTIC_BY_FACTOR"
    }

}