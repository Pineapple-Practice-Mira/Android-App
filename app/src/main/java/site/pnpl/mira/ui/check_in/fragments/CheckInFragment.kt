package site.pnpl.mira.ui.check_in.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import site.pnpl.mira.R
import site.pnpl.mira.data.entity.CheckIn
import site.pnpl.mira.databinding.FragmentCheckInBinding
import site.pnpl.mira.ui.check_in.CheckInCompletedFragment.Companion.CALLBACK_KEY
import site.pnpl.mira.ui.check_in.CheckInViewModel
import site.pnpl.mira.ui.check_in.viewpager.Adapter
import site.pnpl.mira.ui.exercise.convertMillisToDataTimeISO8601

class CheckInFragment : Fragment() {
    private var _binding: FragmentCheckInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckInViewModel by viewModels()
    private lateinit var adapter: Adapter
    private lateinit var viewPager: ViewPager2
    private var emotionId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = Adapter(
            this,
            listOf(
                CheckInFeelFragment(onArrowClickListener),
                CheckInFactorsFragment(onArrowClickListener, onSaveClickListener)
            )
        )
        viewPager = binding.viewPagerFragment.apply {
            adapter = this@CheckInFragment.adapter
            isUserInputEnabled = false
        }

        viewModel.isSaved.observe(viewLifecycleOwner){
            val key = findNavController().currentBackStackEntry?.arguments?.getString(CALLBACK_KEY)
            findNavController().navigate(R.id.action_checkInFragment_to_checkInCompleted, bundleOf(Pair(CALLBACK_KEY, key)))
        }
    }


    private val onArrowClickListener = object : OnArrowClickListener {
        override fun onClick(isForward: Boolean, emotionId: Int) {
            if (isForward) {
                this@CheckInFragment.emotionId = emotionId
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
            } else {
                viewPager.setCurrentItem(viewPager.currentItem - 1, true)
            }
        }
    }

    private val onSaveClickListener = object : OnSaveClickListener {
        override fun onClick(factorId: Int, note: String) {
            println("saved emotionId: $emotionId factorId: $factorId not: $note")
            val checkIn = CheckIn(
                emotionId = emotionId,
                factorId = factorId,
                note = note,
                createdAt = convertMillisToDataTimeISO8601(System.currentTimeMillis()),
                createdAtLong = System.currentTimeMillis()
            )
            viewModel.saveCheckIn(checkIn)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnArrowClickListener {
        fun onClick(isForward: Boolean, emotionId: Int = -1)
    }

    interface OnSaveClickListener {
        fun onClick(factorId: Int, note: String)
    }
}