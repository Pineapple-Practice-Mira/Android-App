package site.pnpl.mira.ui.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentStatisticsByFactorBinding

class StatisticsByFactorFragment : Fragment() {
    private var _binding: FragmentStatisticsByFactorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsByFactorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.close.setOnClickListener {
            findNavController().navigate(R.id.action_stat_by_factor_to_home)
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.checkIn.setOnClickListener {
            findNavController().navigate(R.id.action_stat_by_factor_to_details)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}