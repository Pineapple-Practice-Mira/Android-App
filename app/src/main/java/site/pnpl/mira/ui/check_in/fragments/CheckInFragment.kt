package site.pnpl.mira.ui.check_in.fragments

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import site.pnpl.mira.R
import site.pnpl.mira.data.entity.CheckIn
import site.pnpl.mira.databinding.FragmentCheckInBinding
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_KEY
import site.pnpl.mira.ui.check_in.CheckInViewModel
import site.pnpl.mira.ui.check_in.custoview.BubbleView
import site.pnpl.mira.ui.check_in.viewpager.Adapter
import site.pnpl.mira.ui.exercise.convertMillisToDataTimeISO8601
import site.pnpl.mira.utils.PopUpDialog

class CheckInFragment : Fragment() {
    private var _binding: FragmentCheckInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckInViewModel by viewModels()
    private lateinit var vpAdapter: Adapter
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
        initViewPager()

        requireActivity().window.statusBarColor = resources.getColor(R.color.dark_grey)
        @Suppress("DEPRECATION")
        binding.root.rootWindowInsets.systemWindowInsetBottom
        println("ttttttttttt ${binding.root.rootWindowInsets.systemWindowInsetBottom}")

        viewModel.isSaved.observe(viewLifecycleOwner) {
            val key = findNavController().currentBackStackEntry?.arguments?.getString(CALLBACK_KEY)
            findNavController().navigate(R.id.action_checkInFragment_to_checkInCompleted, bundleOf(Pair(CALLBACK_KEY, key)))
        }
        initBubbleView()
        initPopUpDialog()
    }

    private fun initBubbleView() {
        binding.bubbleView.addListOfBubbles(listOf(
            BubbleView(requireContext(), BubbleView.Type.LEFT, resources.getString(R.string.bubble_1)),
            BubbleView(requireContext(), BubbleView.Type.RIGHT, resources.getString(R.string.bubble_2)),
            BubbleView(requireContext(), BubbleView.Type.LEFT, resources.getString(R.string.bubble_4))
        ))

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
                .build()
            popUpDialog.show(childFragmentManager, PopUpDialog.TAG)
        }
    }

    private val popUpDialogClickListenerLeft = object : PopUpDialog.PopUpDialogClickListener{
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
        const val DURATION_TRANSITION = 650L
    }
}

fun ViewPager2.setCurrentItem(
    index: Int,
    duration: Long,
//    interpolator: TimeInterpolator = PathInterpolator(0.8f, 0f, 0.35f, 1f),
    interpolator: LinearInterpolator = LinearInterpolator(),
    pageWidth: Int = width - paddingLeft - paddingRight,
) {
    val pxToDrag: Int = pageWidth * (index - currentItem)
    val animator = ValueAnimator.ofInt(0, pxToDrag)
    var previousValue = 0

    val scrollView = (getChildAt(0) as? RecyclerView)
    animator.addUpdateListener { valueAnimator ->
        val currentValue = valueAnimator.animatedValue as Int
        val currentPxToDrag = (currentValue - previousValue).toFloat()
        scrollView?.scrollBy(currentPxToDrag.toInt(), 0)
        previousValue = currentValue
    }

    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) {
            // Fallback to fix minor offset inconsistency while scrolling
            setCurrentItem(index, true)
            post { requestTransform() } // To make sure custom transforms are applied
        }

        override fun onAnimationCancel(animation: Animator) { /* Ignored */
        }

        override fun onAnimationRepeat(animation: Animator) { /* Ignored */
        }
    })

    animator.interpolator = interpolator
    animator.duration = duration
    animator.start()
}