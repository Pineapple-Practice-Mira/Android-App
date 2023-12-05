package site.pnpl.mira.ui.statistic.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.SelectedPeriod
import site.pnpl.mira.databinding.FragmentStatisticsBinding
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.model.Emotion
import site.pnpl.mira.model.EmotionsList
import site.pnpl.mira.model.FactorsList
import site.pnpl.mira.ui.statistic.StatisticViewModel
import site.pnpl.mira.ui.statistic.customview.FactorAnalysisView
import site.pnpl.mira.utils.TranslationListener
import javax.inject.Inject

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StatisticViewModel by viewModels()

    @Inject lateinit var selectedPeriod: SelectedPeriod

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().window.sharedElementEnterTransition
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = ANIMATION_TRANSITION_DURATION
            addListener(object : TranslationListener() {
                override fun onTransitionEnd(transition: Transition) {
                    println("onTransitionEnd")
                }
            })
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

        val animationUp = AnimationUtils.loadAnimation(requireContext(), R.anim.check_in_up)
        binding.mainContainer.startAnimation(animationUp)

//        binding.statByFactor.setOnClickListener {
//            findNavController().navigate(R.id.action_statistics_to_statistics_by_factor)
//        }

        initActionBar()
        setClickListener()
        getCheckIns()
        viewModelListener()
    }

    private fun initActionBar() {
        with(binding.actionBar) {
            //Выбранный период для календаря
            initDatePicker(selectedPeriod.startPeriod, selectedPeriod.endPeriod)

            setCalendarPeriodSelectionListener(childFragmentManager) { period ->
                selectedPeriod.startPeriod = period.first
                selectedPeriod.endPeriod = period.second
//                setCallbackFragmentResult()
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

        binding.factorAnalysisContainer.removeAllViews()
        factors.sortByDescending { it.totalCount }
        factors.forEach { factorData ->
            val factorAnalysisView = FactorAnalysisView(
                requireContext(),
                factorName = getString(factorData.nameIdRes),
                positiveCount = factorData.positiveCount,
                negativeCount = factorData.negativeCount
            )

            binding.factorAnalysisContainer.addView(factorAnalysisView)

            factorAnalysisView.startAnimation()
        }
        binding.factorAnalysisContainer.startLayoutAnimation()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ANIMATION_TRANSITION_DURATION = 300L
    }

    private data class FactorData(
        val id: Int,
        @StringRes val nameIdRes: Int,
        var positiveCount: Int = 0,
        var negativeCount: Int = 0,
        var totalCount: Int = 0
    )
}