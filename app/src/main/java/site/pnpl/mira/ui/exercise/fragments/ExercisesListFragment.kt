package site.pnpl.mira.ui.exercise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.models.doOnError
import site.pnpl.mira.data.models.doOnSuccess
import site.pnpl.mira.databinding.FragmentExercisesListBinding
import site.pnpl.mira.domain.EmotionProvider
import site.pnpl.mira.domain.analitycs.Analytics
import site.pnpl.mira.domain.analitycs.AnalyticsEvent
import site.pnpl.mira.domain.analitycs.EventParameter
import site.pnpl.mira.models.ExerciseUI
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_EXERCISES
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_KEY
import site.pnpl.mira.ui.exercise.customview.EmotionButton
import site.pnpl.mira.ui.customview.BottomBar
import site.pnpl.mira.ui.exercise.recyclerview.ExerciseAdapter
import site.pnpl.mira.ui.exercise.ExercisesListViewModel
import site.pnpl.mira.ui.exercise.customview.ItemExercise
import site.pnpl.mira.ui.exercise.recyclerview.SpacingItemDecoration
import site.pnpl.mira.ui.extensions.getParcelableArrayListCompat
import site.pnpl.mira.ui.extensions.screenHeight
import site.pnpl.mira.utils.ANIMATION_DURATION
import javax.inject.Inject

class ExercisesListFragment : Fragment(R.layout.fragment_exercises_list) {
    private var _binding: FragmentExercisesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExercisesListViewModel by viewModels()

    @Inject
    lateinit var emotionProvider: EmotionProvider
    @Inject
    lateinit var analytics: Analytics

    private var exercises: ArrayList<ExerciseUI> = arrayListOf()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private val selectedButtons: MutableList<Int> = mutableListOf()
    private var yPositionLoading: Float = 0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExercisesListBinding.bind(view)
        App.instance.appComponent.inject(this)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)

        binding.popUpLoading.doOnLayout {
            yPositionLoading = it.y
            binding.blackout.isVisible = false
        }

        initProgressBar()
        checkNeedRequest()
        initBottomBar()
        initRecyclerView()
        responseListener()
        setClickListeners()
    }

    private fun initProgressBar() {
        Glide.with(requireContext())
            .asGif()
            .load(R.drawable.progress_bar)
            .into(binding.progressLoading)
    }

    private fun initRecyclerView() {
        adapter = ExerciseAdapter(itemClickListener)
        recyclerView = binding.recyclerView.apply {
            this.adapter = this@ExercisesListFragment.adapter
            addItemDecoration(SpacingItemDecoration(paddingTopInDp = 8))
        }
    }

    private val itemClickListener = object : ExerciseAdapter.ItemClickListener {
        override fun onClick(exerciseUI: ExerciseUI) {
            getExercisesById(exerciseUI)
        }
    }

    private fun getExercisesById(exerciseUI: ExerciseUI) {
        startLoadingExerciseAnimation()
        viewModel.getExerciseById(exerciseUI.id)
    }

    private fun startLoadingExerciseAnimation() {
        binding.apply {
            popUpLoading.apply {
                y = screenHeight.toFloat()
                enableProgressBar(true)
                animate()
                    .alpha(1.0f)
                    .y(yPositionLoading)
                    .setDuration(ANIMATION_DURATION)
                    .start()
            }
            blackout.isVisible = true
        }
    }

    private fun stopLoadingExerciseAnimation() {
        binding.apply {
            popUpLoading.apply {
                enableProgressBar(false)
                setBody(getString(R.string.pop_up_loading_body_2))
                setTextButton(getString(R.string.pop_up_loading_button_2))
                setButtonClickListener {
                    animate()
                        .y(screenHeight.toFloat())
                        .alpha(0f)
                        .setDuration(ANIMATION_DURATION)
                        .withEndAction {
                            blackout.isVisible = false
                        }
                        .start()
                }
            }
        }
    }

    private fun checkNeedRequest() {
        if (exercises.isEmpty()) {
            exercises = arguments?.getParcelableArrayListCompat(EXERCISES_LIST_KEY) ?: arrayListOf()
        }
        if (exercises.isNotEmpty()) {
            onSuccess(exercises)
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
            binding.emotionContainer.scheduleLayoutAnimation()
        }
    }

    private fun filterExercisesList(emotionButton: EmotionButton) {
        if (emotionButton.isSelected) {
            selectedButtons.add(emotionButton.emotionId)
        } else {
            selectedButtons.remove(emotionButton.emotionId)
        }

        if (selectedButtons.isEmpty()) {
            adapter.submitList(exercises.filter { !it.isIntro })
            showNoticeNotFounded(false)
        } else {
            if (exercises.isNotEmpty()) {
                val filteredExercises =
                    exercises.filter { exercise ->
                        exercise.emotionsId.any { it in selectedButtons }
                    }

                adapter.submitList(filteredExercises)
                showNoticeNotFounded(filteredExercises.isEmpty())
            }
        }
        recyclerView.apply {
//            smoothScrollToPosition(0)
            scheduleLayoutAnimation()
        }
    }

    private fun showNoticeNotFounded(value: Boolean) {
        binding.notice.text = getString(R.string.label_exercise_not_found)
        binding.notice.isVisible = value
    }

    private fun putRequests() {
        binding.apply {
            showProgressBar(true)
            notice.isVisible = false
            introExercise.setState(ItemExercise.State.LOADING)
        }
        viewModel.getExerciseList()
    }

    private fun showProgressBar(value: Boolean) {
        binding.progressLoading.isVisible = value
    }

    private fun responseListener() {
        val scope = viewLifecycleOwner.lifecycleScope

        scope.launch {
            viewModel.exerciseList.collect { apiResult ->
                showProgressBar(false)
                apiResult.doOnSuccess { exercises ->
                    @Suppress("UNCHECKED_CAST")
                    onSuccess(exercises as ArrayList<ExerciseUI>)
                }
                apiResult.doOnError {
                    onError()
                }
            }
        }

        scope.launch {
            viewModel.selectedExercise.collect { apiResult ->
                apiResult.doOnSuccess {
                    navigateToExercise(it as ExerciseUI)
                }

                apiResult.doOnError {
                    stopLoadingExerciseAnimation()
                }
            }
        }
    }

    private fun onSuccess(exercises: ArrayList<ExerciseUI>) {
        this.exercises = exercises

        exercises.find { it.isIntro }.apply {
            this?.let {
                setContentToExerciseIntro(it)
            }
        }
        val filteredExercises = exercises.filter { !it.isIntro }

        if (filteredExercises.isEmpty()) {
            binding.notice.apply {
                text = getString(R.string.label_empty)
                isVisible = true
            }
            return
        }

        adapter.submitList(filteredExercises)
        createEmotionButtons()
    }

    private fun setContentToExerciseIntro(exercise: ExerciseUI) {
        binding.introExercise.apply {
            setState(ItemExercise.State.NORMAL, exercise.name)
            setImage(exercise.previewImageLink)
            setClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_EXERCISES_LIST_CLICK_INTRO)
                getExercisesById(exercise)
            }
        }
    }

    private fun onError() {
        binding.apply {
            introExercise.apply {
                setState(ItemExercise.State.ERROR)
            }

            notice.apply {
                text = getString(R.string.label_exercise_loading_error)
                isVisible = true
            }
            update.isVisible = true
        }
    }

    private fun navigateToExercise(exerciseUI: ExerciseUI) {
        analytics.sendEvent(
            AnalyticsEvent.NAME_EXERCISES_LIST_CLICK_INTRO,
            listOf(
                EventParameter(AnalyticsEvent.PARAMETER_EXERCISE_ID, exerciseUI.id),
                EventParameter(AnalyticsEvent.PARAMETER_EXERCISE_NAME, exerciseUI.name),
            )
        )
        val extras = bundleOf(
            Pair(EXERCISE_KEY, exerciseUI),
            Pair(CALLBACK_KEY, CALLBACK_EXERCISES)
        )
        findNavController().navigate(R.id.action_exercises_list_to_exercise, extras)
    }

    private fun setClickListeners() {
        binding.apply {
            update.setOnClickListener {
                it.isVisible = false
                putRequests()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        arguments = bundleOf(Pair(EXERCISES_LIST_KEY, exercises))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXERCISE_KEY = "exercise_key"
        const val EXERCISES_LIST_KEY = "exercises_list"
    }
}