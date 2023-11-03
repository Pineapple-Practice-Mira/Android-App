package site.pnpl.mira.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentExercisesListBinding
import site.pnpl.mira.ui.main.fragments.CheckInCompletedFragment.Companion.CALLBACK_EXERCISES
import site.pnpl.mira.ui.main.fragments.CheckInCompletedFragment.Companion.CALLBACK_KEY

class ExercisesListFragment : Fragment() {
    private var _binding: FragmentExercisesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openHome.setOnClickListener {
            findNavController().navigate(R.id.action_exercises_list_to_home)
        }
        binding.openExercise.setOnClickListener {
            findNavController().navigate(R.id.action_exercises_list_to_exercise)
        }

        binding.createCheckIn.setOnClickListener {
            findNavController().navigate(R.id.action_exercises_list_to_start_check_in, bundleOf(Pair(CALLBACK_KEY, CALLBACK_EXERCISES)))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}