package site.pnpl.mira.ui.check_in.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.text.SpannableStringBuilder
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentCheckInDetailsItemBinding
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.model.Emotion
import site.pnpl.mira.model.EmotionsList
import site.pnpl.mira.model.FactorsList
import site.pnpl.mira.ui.check_in.CheckInDetailsViewModel
import site.pnpl.mira.utils.MiraDateFormat
import site.pnpl.mira.utils.PopUpDialog


class CheckInDetailsItemFragment(
    private val checkIns: List<CheckInUI>,
    private val position: Int,
    private val onArrowClickListener: CheckInDetailsFragment.ArrowClickListener,
) : Fragment(R.layout.fragment_check_in_details_item) {

    private var _binding: FragmentCheckInDetailsItemBinding? = null
    private val binding get() = _binding!!
    private val checkInUI = checkIns[position]
    private var isHidden = false
    private var oldNote: String = ""
    private var newNote: String = ""

    private val viewModel: CheckInDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCheckInDetailsItemBinding.bind(view)

        binding.noteInput.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.noteInput.setRawInputType(InputType.TYPE_CLASS_TEXT)

        setData()
        setClickListeners()
        keyboardListener()
        viewModelListener()
    }

    private fun setData() {
        oldNote = checkInUI.note
        with(binding) {
            val date = MiraDateFormat(checkInUI.createdAtLong)

            tittleDate.text = date.getDayMonthYear()
            tittleDayTime.text = date.getDayOfWeekAndTime()

            emotion.apply {
                text = getString(EmotionsList.emotions[checkInUI.emotionId].nameResId)
                setTextColor(
                    when (EmotionsList.emotions[checkInUI.emotionId].type) {
                        Emotion.Type.POSITIVE -> AppCompatResources.getColorStateList(context, R.color.primary)
                        Emotion.Type.NEGATIVE -> AppCompatResources.getColorStateList(context, R.color.third)
                    }
                )
            }

            emoji.setImageDrawable(AppCompatResources.getDrawable(requireContext(), EmotionsList.emotions[checkInUI.emotionId].emojiResId))
            factor.text = getString(FactorsList.factors[checkInUI.factorId].nameResId)

            noteInput.text = SpannableStringBuilder(checkInUI.note)

            if (position != 0) {
                val prevDateTime = checkIns[position - 1].createdAtLong
                btnBack.text = MiraDateFormat(prevDateTime).getDayMonthTime()
            } else {
                btnBack.text = getString(R.string.no_entry)
                btnBack.isEnabled = false
                arrowBack.isEnabled = false
            }

            if (position != checkIns.size - 1) {
                val nextDateTime = checkIns[position + 1].createdAtLong
                btnNext.text = MiraDateFormat(nextDateTime).getDayMonthTime()
            } else {
                btnNext.text = getString(R.string.no_entry)
                btnNext.isEnabled = false
                arrowNext.isEnabled = false
            }
        }
    }

    private fun setClickListeners() {
        with(binding) {
            close.setOnClickListener {
                findNavController().popBackStack()
            }

            btnBack.setOnClickListener { onArrowClickListener.onClick(CheckInDetailsFragment.Direction.LEFT) }
            arrowBack.setOnClickListener { onArrowClickListener.onClick(CheckInDetailsFragment.Direction.LEFT) }
            btnNext.setOnClickListener { onArrowClickListener.onClick(CheckInDetailsFragment.Direction.RIGHT) }
            arrowNext.setOnClickListener { onArrowClickListener.onClick(CheckInDetailsFragment.Direction.RIGHT) }

            copy.setOnClickListener {
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText(CLIP_LABEL, generateClipText())
                clipboard.setPrimaryClip(clip)
                showPopUpDialogShort(getString(R.string.pop_up_copy_success))
            }

            edit.setOnClickListener {
                noteInput.requestFocus()
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(noteInput, InputMethodManager.SHOW_IMPLICIT)
            }

            delete.setOnClickListener {
                showPopUpDialogBeforeDelete()
            }
        }
    }

    private fun generateClipText(): String {
        val date = MiraDateFormat(checkInUI.createdAtLong)
        return "${getString(R.string.clip_date)}: ${date.getDayMonthYearShort()}, ${date.getTime()}, ${date.getNameDayOfWeek()}\n" +
                "${getString(R.string.clip_feel)}: ${getString(EmotionsList.emotions[checkInUI.emotionId].nameResId)}\n" +
                "${getString(R.string.clip_factor)}: ${getString(FactorsList.factors[checkInUI.factorId].nameResId)}\n" +
                "${getString(R.string.clip_note)}: \"${checkInUI.note}\""
    }

    private fun showPopUpDialogBeforeDelete() {
        val leftButtonClickListener = object : PopUpDialog.PopUpDialogClickListener {
            override fun onClick(popUpDialog: PopUpDialog) {
                popUpDialog.dismiss()
            }
        }

        val rightButtonClickListener = object : PopUpDialog.PopUpDialogClickListener {
            override fun onClick(popUpDialog: PopUpDialog) {
                viewModel.deleteCheckInById(checkInUI)
                popUpDialog.dismiss()
            }
        }

        PopUpDialog.Builder()
            .title(getString(R.string.pop_up_detail_confirm_title))
            .content(getString(R.string.pop_up_detail_confirm_content))
            .leftButtonText(getString(R.string.pop_up_detail_confirm_left_button))
            .rightButtonText(getString(R.string.pop_up_detail_confirm_right_button))
            .leftButtonListener(leftButtonClickListener)
            .rightButtonListener(rightButtonClickListener)
            .animationType(PopUpDialog.AnimationType.RIGHT)
            .build()
            .show(childFragmentManager, PopUpDialog.TAG)
    }

    private fun keyboardListener() {
        ViewCompat.setWindowInsetsAnimationCallback(
            requireView(),
            object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
                override fun onProgress(
                    insets: WindowInsetsCompat,
                    runningAnimations: MutableList<WindowInsetsAnimationCompat>
                ): WindowInsetsCompat = insets

                override fun onStart(
                    animation: WindowInsetsAnimationCompat,
                    bounds: WindowInsetsAnimationCompat.BoundsCompat
                ): WindowInsetsAnimationCompat.BoundsCompat {

                    if (animation.typeMask == WindowInsetsCompat.Type.ime()) {
                        newNote = binding.noteInput.text.toString()
                        if (isHidden && oldNote != newNote) {
                            val newCheckInUI = checkInUI.copy(
                                note = newNote,
                                editedAt = MiraDateFormat(System.currentTimeMillis()).convertToDataTimeISO8601()
                            )
                            viewModel.saveCheckIn(newCheckInUI)
                        }
                        hideAndShowViews(!isHidden)
                    }
                    return super.onStart(animation, bounds)
                }
            }
        )
    }

    private fun hideAndShowViews(isHide: Boolean) {
        with(binding) {
            emotionContainer.isVisible = !isHide
            factorContainer.isVisible = !isHide
            copy.isVisible = !isHide
            edit.isVisible = !isHide
            delete.isVisible = !isHide
            bottomBar.isVisible = !isHide
            line.isVisible = !isHide
            arrowBack.isVisible = !isHide
            btnBack.isVisible = !isHide
            arrowNext.isVisible = !isHide
            btnNext.isVisible = !isHide
            mountains.isVisible = !isHide
        }
        isHidden = isHide
    }

    private fun viewModelListener() {
        viewModel.isSaved.observe(viewLifecycleOwner) {
            if (it) {
                oldNote = newNote
                showPopUpDialogShort(getString(R.string.pop_up_save_success))
            }
        }

        viewModel.isDelete.observe(viewLifecycleOwner) {
            if (it) {
                val dialogClickListener = object : PopUpDialog.PopUpDialogClickListener {
                    override fun onClick(popUpDialog: PopUpDialog) {
                        popUpDialog.dismiss()
                        findNavController().popBackStack()
                    }
                }

                PopUpDialog.Builder()
                    .title(getString(R.string.pop_up_detail_delete_title))
                    .content(getString(R.string.pop_up_detail_delete_content))
                    .leftButtonText(getString(R.string.pop_up_detail_delete_button))
                    .leftButtonListener(dialogClickListener)
                    .build()
                    .show(childFragmentManager, PopUpDialog.TAG)

            }
        }
    }

    private fun showPopUpDialogShort(message: String) {
        PopUpDialog.Builder()
            .content(message)
            .duration(1500)
            .animationType(PopUpDialog.AnimationType.BOTTOM)
            .build()
            .show(childFragmentManager, PopUpDialog.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CLIP_LABEL = "CheckIn Info"
    }
}