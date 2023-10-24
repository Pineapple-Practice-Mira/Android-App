package site.pnpl.mira.ui.greeting.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import site.pnpl.mira.ui.greeting.GreetingActivity
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentGreetingBinding

class GreetingFragment : Fragment() {
    private var _binding: FragmentGreetingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGreetingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.next.setOnClickListener {
            (activity as GreetingActivity).navController.navigate(R.id.action_greetingFragment_to_acquaintanceFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}