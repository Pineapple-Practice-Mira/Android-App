package site.pnpl.mira.ui.check_in.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.database.check_in.entity.CheckIn
import site.pnpl.mira.data.models.doOnError
import site.pnpl.mira.data.models.doOnSuccess
import site.pnpl.mira.databinding.FragmentCheckInSavedBinding
import site.pnpl.mira.domain.EmotionProvider
import site.pnpl.mira.domain.analitycs.Analytics
import site.pnpl.mira.domain.analitycs.AnalyticsEvent
import site.pnpl.mira.domain.analitycs.EventParameter
import site.pnpl.mira.models.ExerciseUI
import site.pnpl.mira.models.FactorsList
import site.pnpl.mira.ui.check_in.CheckInSavedViewModel
import site.pnpl.mira.ui.check_in.customview.BubbleView
import site.pnpl.mira.ui.exercise.customview.ItemExercise
import site.pnpl.mira.ui.exercise.fragments.ExercisesListFragment
import site.pnpl.mira.ui.extensions.getParcelableCompat
import site.pnpl.mira.utils.MiraDateFormat
import javax.inject.Inject

class CheckInSavedFragment : Fragment(R.layout.fragment_check_in_saved) {

    private var _binding: FragmentCheckInSavedBinding? = null
    private val binding: FragmentCheckInSavedBinding get() = _binding!!

    @Inject
    lateinit var emotionProvider: EmotionProvider

    @Inject
    lateinit var analytics: Analytics
    private val viewModel: CheckInSavedViewModel by viewModels()

    private var exerciseCache: ExerciseUI? = null
    private var checkIn: CheckIn? = null
    private var callbackKey: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

        _binding = FragmentCheckInSavedBinding.bind(view)
        callbackKey = findNavController().currentBackStackEntry?.arguments?.getString(CALLBACK_KEY)

        checkIn = findNavController().currentBackStackEntry?.arguments?.getParcelableCompat(CHECK_IN_KEY)


        setClickListener()
        initScrollBubbleView()

        checkIn?.emotionId?.let { emotionProvider.open(it) }
        responseListener()
        checkNeedRequest()
    }

    private fun checkNeedRequest() {
        if (exerciseCache != null) {
            onSuccess(exerciseCache!!)
        } else {
            putRequests()
        }
    }

    private fun putRequests() {
        binding.exercise.setState(ItemExercise.State.LOADING)
        checkIn?.let {
            viewModel.getExerciseByEmotionId(checkIn!!.emotionId)
        }
    }

    private fun responseListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exerciseFlow.collect { apiResult ->
                apiResult.doOnSuccess { exercise ->
                    onSuccess(exercise as ExerciseUI)
                }
                apiResult.doOnError {
                    onError(it)
                }
            }
        }
    }

    private fun onSuccess(exercise: ExerciseUI) {
        exerciseCache = exercise
        binding.exercise.apply {
            setState(ItemExercise.State.NORMAL, exercise.name)
            setImage(exercise.previewImageLink)
            setClickListener {
                analytics.sendEvent(
                    AnalyticsEvent.NAME_CHECK_IN_EXERCISE_CLICK,
                    listOf(
                        EventParameter(AnalyticsEvent.PARAMETER_EXERCISE_ID, exercise.id),
                        EventParameter(AnalyticsEvent.PARAMETER_EXERCISE_NAME, exercise.name),
                    )
                )
                navigateToExercise(exercise)
            }
        }
    }

    private fun navigateToExercise(exerciseUI: ExerciseUI) {
        val extras = bundleOf(
            Pair(ExercisesListFragment.EXERCISE_KEY, exerciseUI),
            Pair(
                CALLBACK_KEY,
                if (callbackKey == CALLBACK_HOME) callbackKey else CALLBACK_EXERCISES_UPDATE
            )
        )
        findNavController().navigate(R.id.action_check_in_saved_to_exercise_fragment, extras)
    }

    private fun onError(it: String?) {
        println(it)
        binding.exercise.apply {
            setState(ItemExercise.State.ERROR_WITH_REFRESH)
            setRefreshClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_CHECK_IN_EXERCISE_REFRESH_CLICK)
                putRequests()
            }
        }
    }

    private fun setClickListener() {
        binding.apply {
            close.setOnClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_CHECK_IN_SAVED_CLOSE)
                navigateByKey(callbackKey)
            }
            later.setOnClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_CHECK_IN_SAVED_CLOSE_VIA_BUTTON)
                navigateByKey(callbackKey)
            }
        }
    }

    private fun navigateByKey(key: String?) {
        when (key) {
            CALLBACK_EXERCISES_NON_UPDATE -> findNavController().navigate(R.id.action_checkInCompleted_to_exercises_list)
            else -> findNavController().navigate(R.id.action_checkInCompleted_to_navigation_home)
        }
    }

    private fun initScrollBubbleView() {
        checkIn?.let {
            binding.scrollBubbleView.addListOfBubbles(
                listOf(
                    BubbleView(
                        requireContext(),
                        BubbleView.Type.RIGHT_HIGH,
                        getString(
                            R.string.bubble_5,
                            MiraDateFormat(checkIn!!.createdAtLong).getDayMonthYear(),
                            emotionProvider.getNameGenitive(checkIn!!.emotionId),
                            getString(FactorsList.factors[checkIn!!.factorId].nameResId)
                        )
                    ),
                    BubbleView(requireContext(), BubbleView.Type.LEFT_SMALL, getString(R.string.bubble_6))
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CALLBACK_KEY = "CALLBACK_KEY"
        const val CALLBACK_HOME = "HOME"
        const val CALLBACK_EXERCISES_NON_UPDATE = "EXERCISES_NON_UPDATE"
        const val CALLBACK_EXERCISES_UPDATE = "EXERCISES_UPDATE"
        const val CHECK_IN_KEY = "CHECK_IN"
    }
}