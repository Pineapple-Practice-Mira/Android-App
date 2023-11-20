package site.pnpl.mira.ui.exercise

import android.os.Bundle
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
//        Glide.with(requireContext())
//            .asGif()
//            .load(URL("https://media4.giphy.com/media/v1.Y2lkPTc5MGI3NjExeGhkd3N5MTdvOHF5Z3VlOGpybTBzcnhvNzYzYnV1cWE3MjV0dHd5bCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/2yLNN4wTy7Zr8JSXHB/giphy.gif"))
//            .into(binding.gif)

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