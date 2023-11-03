package site.pnpl.mira.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentCheckInFeelingBinding
import site.pnpl.mira.ui.main.fragments.CheckInCompletedFragment.Companion.CALLBACK_KEY

class CheckInFeelFragment : Fragment() {
    private var _binding: FragmentCheckInFeelingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckInFeelingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val key = findNavController().currentBackStackEntry?.arguments?.getString(CALLBACK_KEY)
        binding.next.setOnClickListener {
            findNavController().navigate(R.id.action_feel_to_factors, bundleOf(Pair(CALLBACK_KEY, key)))
        }
        binding.close.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}