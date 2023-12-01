package site.pnpl.mira.ui.statistic

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.fragment.app.setFragmentResult
import androidx.transition.ChangeBounds
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentStatisticsBinding
import site.pnpl.mira.ui.home.fragments.HomeFragment.Companion.KEY_END_PERIOD
import site.pnpl.mira.ui.home.fragments.HomeFragment.Companion.KEY_START_PERIOD
import site.pnpl.mira.ui.home.fragments.HomeFragment.Companion.SELECTED_PERIOD

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private var startPeriod = 0L
    private var endPeriod = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = ANIMATION_TRANSITION_DURATION
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = ANIMATION_TRANSITION_DURATION
        }
        _binding = FragmentStatisticsBinding.inflate(layoutInflater, container, false)

        startPeriod = arguments?.getLong(KEY_START_PERIOD)!!
        endPeriod = arguments?.getLong(KEY_END_PERIOD)!!

        setFragmentResult(
            SELECTED_PERIOD,
            bundleOf(
                kotlin.Pair(KEY_START_PERIOD, startPeriod),
                kotlin.Pair(KEY_END_PERIOD, endPeriod)
            )
        )

        binding.actionBar.setSelectedPeriod(Pair(startPeriod, endPeriod))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.dark_grey)

        val animationUp = AnimationUtils.loadAnimation(requireContext(), R.anim.check_in_up)
        binding.mainContainer.startAnimation(animationUp)

//        binding.statByFactor.setOnClickListener {
//            findNavController().navigate(R.id.action_statistics_to_statistics_by_factor)
//        }
        binding.close.setOnClickListener {
            val animationDown = AnimationUtils.loadAnimation(requireContext(), R.anim.check_in_down)
            binding.mainContainer.startAnimation(animationDown)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ANIMATION_TRANSITION_DURATION = 300L
    }
}