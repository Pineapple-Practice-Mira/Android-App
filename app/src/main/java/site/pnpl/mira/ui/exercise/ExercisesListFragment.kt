package site.pnpl.mira.ui.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentExercisesListBinding
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_EXERCISES
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_KEY
import site.pnpl.mira.ui.exercise.customview.EmotionButton
import site.pnpl.mira.ui.customview.BottomBar

class ExercisesListFragment : Fragment(R.layout.fragment_exercises_list) {
    private var _binding: FragmentExercisesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExercisesListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExercisesListBinding.bind(view)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)

//        stubClickListener()
        initBottomBar()
        fillEmotionButtons()
    }

    private fun fillEmotionButtons() {
        binding.emotionContainer.apply {
            addView(EmotionButton(requireContext(), "Радость"))
            addView(EmotionButton(requireContext(), "Любовь"))
            addView(EmotionButton(requireContext(), "Грусть"))
            addView(EmotionButton(requireContext(), "Злость"))
            addView(EmotionButton(requireContext(), "Верность"))
            addView(EmotionButton(requireContext(), "Тоска"))
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

//    private fun stubClickListener() {
//        binding.openExercise.setOnClickListener {
//            findNavController().navigate(R.id.action_exercises_list_to_exercise)
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}