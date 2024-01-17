package site.pnpl.mira.ui.exercise.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentExerciseBinding
import site.pnpl.mira.domain.EmotionProvider
import site.pnpl.mira.models.ExerciseUI
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_HOME
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_KEY
import site.pnpl.mira.ui.exercise.fragments.ExercisesListFragment.Companion.EXERCISE_KEY
import site.pnpl.mira.ui.exercise.customview.EmotionButton
import site.pnpl.mira.ui.greeting.fragments.GreetingFragment.Companion.SCREENS_KEY
import site.pnpl.mira.utils.GlideListener
import javax.inject.Inject

class ExerciseFragment : Fragment(R.layout.fragment_exercise) {
    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var emotionProvider: EmotionProvider

    private var exerciseUI: ExerciseUI? = null
    private var callbackKey: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExerciseBinding.bind(view)
        App.instance.appComponent.inject(this)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.dark_grey)

        setClickListener()

        callbackKey = findNavController().currentBackStackEntry?.arguments?.getString(CALLBACK_KEY)

        exerciseUI = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(EXERCISE_KEY, ExerciseUI::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(EXERCISE_KEY)
        }


        exerciseUI?.let {
            setContentOnUi(it)
            if (it.emotionsId.isEmpty()) {
                hideUIElements()
            } else {
                createEmotionButtons(it)
            }
        }
    }

    private fun setContentOnUi(exerciseUI: ExerciseUI) {
        showProgressBar(true)
        binding.apply {
            Glide.with(requireContext())
                .load(exerciseUI.previewImageLink)
//                .thumbnail(Glide.with(requireContext()).load(R.drawable.progress_bar))
                .listener(GlideListener.OnCompletedDrawable{
                    showProgressBar(false)
                })
                .into(preview)

            exerciseName.text = exerciseUI.name
            description.text = exerciseUI.description
        }
    }

    private fun hideUIElements() {
        binding.apply {
            emotionScrollView.isVisible = false
            subhead.isVisible = false
        }
    }

    private fun createEmotionButtons(exerciseUI: ExerciseUI) {
        exerciseUI.emotionsId.forEach { emotionId ->
            val emotionButton = EmotionButton(requireContext(), emotionProvider.getName(emotionId), emotionId).apply {
                isEnabled = false
                isClickable = false
            }
            binding.emotionContainer.addView(emotionButton)
        }
    }

    private fun setClickListener() {
        binding.close.setOnClickListener {
            when (callbackKey) {
                CALLBACK_HOME -> findNavController().navigate(R.id.action_exercise_fragment_to_home)
                else -> findNavController().navigate(R.id.action_exercise_fragment_to_exercise_list)
            }
        }

        binding.startButton.setOnClickListener {
            val bundle = bundleOf(
                Pair(SCREENS_KEY, exerciseUI?.screens),
                Pair(CALLBACK_KEY, callbackKey)
            )
            findNavController().navigate(R.id.action_exerciseFragment_to_exerciseDetailsFragment, bundle)
        }
    }

    private fun showProgressBar(value: Boolean) {
        binding.progressBar.isVisible = value
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}