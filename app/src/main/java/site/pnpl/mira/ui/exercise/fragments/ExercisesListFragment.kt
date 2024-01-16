package site.pnpl.mira.ui.exercise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.models.doOnError
import site.pnpl.mira.data.models.doOnSuccess
import site.pnpl.mira.databinding.FragmentExercisesListBinding
import site.pnpl.mira.domain.EmotionProvider
import site.pnpl.mira.models.ExerciseUI
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_EXERCISES
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_KEY
import site.pnpl.mira.ui.exercise.customview.EmotionButton
import site.pnpl.mira.ui.customview.BottomBar
import site.pnpl.mira.ui.exercise.ExercisesListViewModel
import site.pnpl.mira.ui.exercise.customview.ItemExercise
import javax.inject.Inject

class ExercisesListFragment : Fragment(R.layout.fragment_exercises_list) {
    private var _binding: FragmentExercisesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExercisesListViewModel by viewModels()

    @Inject
    lateinit var emotionProvider: EmotionProvider

    private var exerciseIntro: ExerciseUI? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExercisesListBinding.bind(view)
        App.instance.appComponent.inject(this)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)

        initBottomBar()
        createEmotionButtons()
        checkNeedRequest()
        responseListener()

    }

    private fun checkNeedRequest() {
        if (exerciseIntro != null) {
            onSuccess(exerciseIntro!!)
        } else {
            putRequests()
        }
    }

    private fun initBottomBar() {
        binding.bottomBar.setSelectedButton(BottomBar.EXERCISES_LIST)
        binding.bottomBar.setBottomBarClickListener { button ->
            when (button) {
                BottomBar.Button.HOME -> {
                    findNavController().navigate(R.id.action_exercises_list_to_home)
                }

                BottomBar.Button.EXERCISES_LIST -> {}
                BottomBar.Button.CHECK_IN -> {
                    findNavController().navigate(R.id.action_exercises_list_to_start_check_in, bundleOf(Pair(CALLBACK_KEY, CALLBACK_EXERCISES)))
                }
            }
        }
    }

    private fun createEmotionButtons() {
        val openEmotions = emotionProvider.emotions.filter { it.isOpened }
        if (openEmotions.isNotEmpty()) {
            openEmotions.forEach { emotionUI ->
                val emotionButton = EmotionButton(requireContext(), emotionUI.name, emotionUI.id)
                emotionButton.onClickListener { clickedEmotionButton ->
                    filterExercisesList(clickedEmotionButton)
                }
                binding.emotionContainer.addView(emotionButton)
            }
        }
    }

    private fun filterExercisesList(emotionButton: EmotionButton) {
        println(emotionButton.emotionId)
    }

    private fun putRequests() {
        binding.introExercise.setState(ItemExercise.State.LOADING)
        viewModel.getExerciseIntro()
    }

    private fun responseListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exerciseIntro.collect { apiResult ->
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
        exerciseIntro = exercise
        binding.introExercise.apply {
            setState(ItemExercise.State.NORMAL, exercise.name)
            setImage(exercise.previewImageLink)
            setClickListener {
                navigateToExercise(exercise)
            }
        }
    }

    private fun onError(it: String?) {
        binding.introExercise.apply {
            setState(ItemExercise.State.ERROR_WITH_REFRESH)
            setRefreshClickListener {
                putRequests()
            }
        }
    }

    private fun navigateToExercise(exerciseUI: ExerciseUI) {
        val extras = bundleOf(Pair(EXERCISE_KEY, exerciseUI))
        findNavController().navigate(R.id.action_exercises_list_to_exercise, extras)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXERCISE_KEY = "exercise_key"
    }
}