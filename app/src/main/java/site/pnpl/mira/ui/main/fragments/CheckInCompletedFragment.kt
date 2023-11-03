package site.pnpl.mira.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentCheckInComletedBinding

class CheckInCompletedFragment : Fragment() {
    private var _binding: FragmentCheckInComletedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckInComletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val key = findNavController().currentBackStackEntry?.arguments?.getString(CALLBACK_KEY)
        binding.main .setOnClickListener {
            when (key) {
                CALLBACK_EXERCISES -> findNavController().navigate(R.id.action_checkInCompleted_to_exercises_list)
                else -> findNavController().navigate(R.id.action_checkInCompleted_to_navigation_home)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CALLBACK_KEY = "CALLBACK_KEY"
        const val CALLBACK_HOME = "HOME"
        const val CALLBACK_EXERCISES = "EXERCISES"
    }
}