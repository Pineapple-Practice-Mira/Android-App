package site.pnpl.mira.ui.check_in.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentCheckInDetailsBinding
import site.pnpl.mira.models.CheckInUI
import site.pnpl.mira.ui.check_in.viewpager.DetailAdapter
import site.pnpl.mira.ui.extensions.getParcelableArrayListCompat
import site.pnpl.mira.ui.extensions.setCurrentItem

class CheckInDetailsFragment : Fragment(R.layout.fragment_check_in_details) {
    private var _binding: FragmentCheckInDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewPager: ViewPager2 by lazy {
        binding.viewPager
    }

    private var position: Int? = null
    private var checkIns: List<CheckInUI>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCheckInDetailsBinding.bind(view)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.dark_grey)

        with(findNavController().currentBackStackEntry?.arguments) {
            this?.let {
                position = getInt(POSITION_KEY)
                checkIns = getParcelableArrayListCompat(LIST_OF_CHECK_IN_KEY)
            }
        }

        initViewPager()
    }

    private fun initViewPager() {
        viewPager.apply {
            adapter = DetailAdapter(this@CheckInDetailsFragment, checkIns!!, onArrowClickListener)
            isUserInputEnabled = false
            setCurrentItem(position!!, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val onArrowClickListener = object : ArrowClickListener {
        override fun onClick(direction: Direction) {
            when(direction) {
                Direction.LEFT -> {
                    viewPager.setCurrentItem(viewPager.currentItem - 1, ANIMATION_DURATION)
                }
                Direction.RIGHT -> {
                    viewPager.setCurrentItem(viewPager.currentItem + 1, ANIMATION_DURATION)
                }
            }
        }

    }
    interface ArrowClickListener {
        fun onClick(direction: Direction)
    }

    companion object {
        const val CALLBACK_KEY = "CALLBACK_KEY"
        const val CALLBACK_HOME = "HOME"
        const val CALLBACK_STATISTIC = "STATISTIC"
        const val POSITION_KEY = "POSITION"
        const val LIST_OF_CHECK_IN_KEY = "CHECK_INS"
        const val ANIMATION_DURATION = 300L
    }

    enum class Direction {
        LEFT,
        RIGHT
    }

}