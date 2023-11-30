package site.pnpl.mira.ui.check_in.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import site.pnpl.mira.R
import site.pnpl.mira.data.entity.CheckIn
import site.pnpl.mira.databinding.FragmentCheckInBinding
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_KEY
import site.pnpl.mira.ui.check_in.CheckInViewModel
import site.pnpl.mira.ui.check_in.customview.BubbleView
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CHECK_IN_KEY
import site.pnpl.mira.ui.check_in.viewpager.Adapter
import site.pnpl.mira.utils.MiraDateFormat
import site.pnpl.mira.utils.PopUpDialog
import site.pnpl.mira.utils.setCurrentItem

class CheckInFragment : Fragment(R.layout.fragment_check_in) {
    private var _binding: FragmentCheckInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckInViewModel by viewModels()
    private lateinit var vpAdapter: Adapter
    private lateinit var viewPager: ViewPager2
    private var emotionId = -1
    private var checkIn: CheckIn? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCheckInBinding.bind(view)
        initViewPager()

        @Suppress("DEPRECATION")
        requireActivity().window.statusBarColor = resources.getColor(R.color.dark_grey)

        viewModel.isSaved.observe(viewLifecycleOwner) {
            val key = findNavController().currentBackStackEntry?.arguments?.getString(CALLBACK_KEY)
            findNavController()
                .navigate(
                    R.id.action_checkInFragment_to_checkInCompleted,
                    bundleOf(
                        Pair(CALLBACK_KEY, key),
                        Pair(CHECK_IN_KEY, checkIn)
                    )
                )
        }
        initBubbleView()
        initPopUpDialog()
    }

    private fun initBubbleView() {
        binding.bubbleView.addListOfBubbles(
            listOf(
                BubbleView(requireContext(), BubbleView.Type.LEFT_HIGH, resources.getString(R.string.bubble_1)),
                BubbleView(requireContext(), BubbleView.Type.RIGHT_SMALL, resources.getString(R.string.bubble_2)),
                BubbleView(requireContext(), BubbleView.Type.LEFT_HIGH, resources.getString(R.string.bubble_4))
            )
        )

    }

    private fun initViewPager() {
        vpAdapter = Adapter(
            this,
            listOf(
                CheckInFeelFragment(onArrowClickListener, onEmotionClickListener),
                CheckInFactorsFragment(onArrowClickListener, onSaveClickListener)
            )
        )
        viewPager = binding.viewPagerFragment.apply {
            adapter = this@CheckInFragment.vpAdapter
            isUserInputEnabled = false
        }
    }

    private fun initPopUpDialog() {
        binding.close.setOnClickListener {
            val popUpDialog = PopUpDialog.Builder()
                .title(resources.getString(R.string.pop_up_check_in_title))
                .content(resources.getString(R.string.pop_up_check_in_content))
                .leftButtonText(resources.getString(R.string.pop_up_check_in_left))
                .rightButtonText((resources.getString(R.string.pop_up_check_in_right)))
                .leftButtonListener(popUpDialogClickListenerLeft)
                .rightButtonListener(popUpDialogClickListenerRight)
                .animationType(PopUpDialog.AnimationType.RIGHT)
                .build()
            popUpDialog.show(childFragmentManager, PopUpDialog.TAG)
        }
    }

    private val popUpDialogClickListenerLeft = object : PopUpDialog.PopUpDialogClickListener {
        override fun onClick(popUpDialog: PopUpDialog) {
            popUpDialog.dismiss()
        }
    }

    private val popUpDialogClickListenerRight = object : PopUpDialog.PopUpDialogClickListener {
        override fun onClick(popUpDialog: PopUpDialog) {
            findNavController().popBackStack()
        }

    }

    private val onArrowClickListener = object : OnArrowClickListener {
        override fun onClick(isForward: Boolean, emotionId: Int) {
            if (isForward) {
                this@CheckInFragment.emotionId = emotionId
                viewPager.setCurrentItem(viewPager.currentItem + 1, DURATION_TRANSITION)
                binding.bubbleView.scrollUp()
            } else {
                viewPager.setCurrentItem(viewPager.currentItem - 1, DURATION_TRANSITION)
                binding.bubbleView.scrollDown()
            }
        }
    }

    private val onSaveClickListener = object : OnSaveClickListener {
        override fun onClick(factorId: Int, note: String) {
            checkIn = CheckIn(
                emotionId = emotionId,
                factorId = factorId,
                note = note,
                createdAt = MiraDateFormat(System.currentTimeMillis()).convertToDataTimeISO8601(),
                createdAtLong = System.currentTimeMillis()
            )
            viewModel.saveCheckIn(checkIn!!)
        }

    }

    private val onEmotionClickListener = object : OnEmotionClickListener {
        override fun onClick(emotionName: String?) {
            var message = resources.getString(R.string.bubble_2)
            if (emotionName != null) {
                message = resources.getString(R.string.bubble_3) + emotionName
            }
            binding.bubbleView.setMessageInRightBubble(message)
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

    interface OnEmotionClickListener {
        fun onClick(emotionName: String?)
    }

    companion object {
        const val DURATION_TRANSITION = 300L
    }
}