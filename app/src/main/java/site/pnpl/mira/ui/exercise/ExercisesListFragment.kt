package site.pnpl.mira.ui.exercise

import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentExercisesListBinding
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_EXERCISES
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_KEY
import site.pnpl.mira.ui.customview.BottomBar
import java.util.Calendar

class ExercisesListFragment : Fragment() {
    private var _binding: FragmentExercisesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExercisesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stubClickListener()
        @Suppress("DEPRECATION")
        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
        initBottomBar()
        viewModel.isSaved.observe(viewLifecycleOwner) {
            val text = if (it) "сохранено" else "НЕ сохранено"
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }

    }

    private fun initBottomBar() {
        binding.bottomBar.setSelectedButton(BottomBar.EXERCISES_LIST)
        binding.bottomBar.setClickListener(object : BottomBar.BottomBarClicked {
            override fun onClick(button: BottomBar.BottomBarButton) {
                when (button) {
                    BottomBar.BottomBarButton.HOME -> {
                        findNavController().navigate(R.id.action_exercises_list_to_home)
                    }

                    BottomBar.BottomBarButton.EXERCISES_LIST -> {}
                    BottomBar.BottomBarButton.CHECK_IN -> {
                        findNavController().navigate(R.id.action_exercises_list_to_start_check_in, bundleOf(Pair(CALLBACK_KEY, CALLBACK_EXERCISES)))
                    }
                }
            }
        })
    }

    private fun stubClickListener() {
        binding.openExercise.setOnClickListener {
            findNavController().navigate(R.id.action_exercises_list_to_exercise)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun convertMillisToDataTimeISO8601(millis: Long): String {
    val calendar = Calendar.Builder()
        .setInstant(millis)
        .build()
    return DateFormat.format("yyyy-MM-dd'T'HH:mm:ss", calendar.time).toString()
}