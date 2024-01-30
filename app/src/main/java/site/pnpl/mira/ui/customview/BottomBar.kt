package site.pnpl.mira.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintLayout
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.databinding.BottomBarBinding
import site.pnpl.mira.domain.analitycs.Analytics
import site.pnpl.mira.domain.analitycs.AnalyticsEvent
import javax.inject.Inject

class BottomBar constructor(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {

    private var _binding: BottomBarBinding? = null
    private val binding: BottomBarBinding get() = _binding!!
    @Inject lateinit var analytics: Analytics

    private val buttons: List<View> by lazy {
        listOf(binding.home, binding.exercisesList)
    }

    init {
        _binding = BottomBarBinding.bind(LayoutInflater.from(context).inflate(R.layout.bottom_bar, this))
        App.instance.appComponent.inject(this)
    }

    fun setSelectedButton(@BottomButton bottomButton: Int) {
        buttons.forEach { it.isSelected = false }
        buttons[bottomButton].isSelected = true

    }

    fun setBottomBarClickListener(listener: (Button) -> Unit) {
        with(binding) {
            home.setOnClickListener { listener(Button.HOME) }
            exercisesList.setOnClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_BOTTOM_BAR_CLICK_EXERCISE_LIST)
                listener(Button.EXERCISES_LIST)
            }
            btnCheckIn.setOnClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_BOTTOM_BAR_CLICK_CHECK_IN)
                listener(Button.CHECK_IN)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
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

    enum class Button {
        HOME,
        EXERCISES_LIST,
        CHECK_IN
    }

}

