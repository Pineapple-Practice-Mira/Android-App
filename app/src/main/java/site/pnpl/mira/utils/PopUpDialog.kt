package site.pnpl.mira.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import site.pnpl.mira.R
import site.pnpl.mira.databinding.PopUpDialogBinding

class PopUpDialog private constructor(
    private val title: String = "",
    private val content: String = "",
    private val rightButtonText: String = "",
    private val rightButtonListener: PopUpDialogClickListener? = null,
    private val leftButtonText: String = "",
    private val leftButtonListener: PopUpDialogClickListener? = null
) : DialogFragment(R.layout.pop_up_dialog) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        PopUpDialogBinding.bind(view).apply {
            title.text = this@PopUpDialog.title
            content.text = this@PopUpDialog.content

            leftButton.apply {
                text = leftButtonText
                background = AppCompatResources.getDrawable(requireContext(), R.drawable.pop_up_button_background)
            }
            rightButton.apply {
                text = rightButtonText
                background = AppCompatResources.getDrawable(requireContext(), R.drawable.pop_up_button_background)
            }

            leftButton.setOnClickListener {
                leftButtonListener?.onClick(this@PopUpDialog)
            }

            rightButton.setOnClickListener {
                rightButtonListener?.onClick(this@PopUpDialog)
            }
        }
    }

    companion object {
        const val TAG = "POP_UP_DIALOG"
    }

    interface PopUpDialogClickListener {
        fun onClick(popUpDialog: PopUpDialog)
    }

    data class Builder(
        var title: String = "",
        var content: String = "",
        var rightButtonText: String = "",
        var rightButtonListener: PopUpDialogClickListener? = null,
        var leftButtonText: String = "",
        var leftButtonListener: PopUpDialogClickListener? = null
    ) {
        fun title(title: String) = apply { this.title = title }
        fun content(content: String) = apply { this.content = content }
        fun rightButtonText(rightButtonText: String) = apply { this.rightButtonText = rightButtonText }
        fun rightButtonListener(rightButtonListener: PopUpDialogClickListener?) = apply { this.rightButtonListener = rightButtonListener }
        fun leftButtonText(leftButtonText: String) = apply { this.leftButtonText = leftButtonText }
        fun leftButtonListener(leftButtonListener: PopUpDialogClickListener?) = apply { this.leftButtonListener = leftButtonListener }

        fun build() = PopUpDialog(title, content, rightButtonText, rightButtonListener, leftButtonText, leftButtonListener)
    }
}