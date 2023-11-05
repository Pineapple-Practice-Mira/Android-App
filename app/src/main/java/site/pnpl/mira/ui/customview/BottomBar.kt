package site.pnpl.mira.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintLayout
import site.pnpl.mira.R
import site.pnpl.mira.databinding.BottomBarBinding

class BottomBar constructor(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {

    private var _binding: BottomBarBinding? = null
    private val binding: BottomBarBinding get() = _binding!!

    private val buttons: List<View> by lazy {
        listOf(binding.home, binding.exercisesList)
    }

    init {
        _binding = BottomBarBinding.bind(LayoutInflater.from(context).inflate(R.layout.bottom_bar, this))
    }

    fun setFocusedButton(@BottomButton bottomButton: Int) {
        buttons.forEach { isSelected = false }
        buttons[bottomButton].isSelected = true

    }

    fun setClickListener(listener: BottomBarClicked) {
        binding.home.setOnClickListener {
            listener.onClick(BottomBarButton.HOME)
        }

        binding.exercisesList.setOnClickListener {
            listener.onClick(BottomBarButton.EXERCISES_LIST)
        }

        binding.btnCheckIn.setOnClickListener {
            listener.onClick(BottomBarButton.CHECK_IN)
        }

    }

    companion object {
        const val HOME = 0
        const val EXERCISES_LIST = 1

        @IntDef(
            value = [
                HOME,
                EXERCISES_LIST
            ]
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class BottomButton
    }

    interface BottomBarClicked {
        fun onClick(button: BottomBarButton)
    }

    enum class BottomBarButton {
        HOME,
        EXERCISES_LIST,
        CHECK_IN
    }

}

