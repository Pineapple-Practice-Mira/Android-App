package site.pnpl.mira.ui.home.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.core.util.Pair
import androidx.core.view.isVisible
import site.pnpl.mira.R
import site.pnpl.mira.databinding.ActionBarBinding
import site.pnpl.mira.utils.MiraDateFormat

class ActionBar(context: Context, attributeSet: AttributeSet) : CardView(context, attributeSet) {

    private var _binding: ActionBarBinding? = null
    private val binding: ActionBarBinding get() = _binding!!

    init {
        _binding = ActionBarBinding.bind(LayoutInflater.from(context).inflate(R.layout.action_bar, this))
    }

    fun setActionBarClickListener(listener: (Button) -> Unit) {
        with(binding) {
            calendar.setOnClickListener { listener(Button.CALENDAR) }
            statistic.setOnClickListener { listener(Button.STATISTIC) }
            selector.setOnClickListener {
                selector.isSelected = !selector.isSelected
                listener(Button.SELECT_ALL)
            }
            delete.setOnClickListener { listener(Button.DELETE) }
        }
    }

    fun setSelectedPeriod(periods: Pair<Long, Long>) {
        val dateFirst = MiraDateFormat(periods.first)
        val dateSecond = MiraDateFormat(periods.second)
        val periodString =
            "${dateFirst.getNameMonthUpper()} ${dateFirst.getDateOfMonth()} - ${dateSecond.getNameMonthUpper()} ${dateSecond.getDateOfMonth()}"
        binding.period.text = periodString
    }

    fun setRemoveMode(value: Boolean) {
        with(binding) {
            calendar.isVisible = !value
            statistic.isVisible = !value
            selector.isVisible = value
            delete.isVisible = value
            delete.isEnabled = false
        }
        if(!value) {
            binding.selector.isSelected = false
        }
    }

    fun trashBoxEnable(value: Boolean) {
        binding.delete.isEnabled = value
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
    enum class Button {
        CALENDAR,
        STATISTIC,
        SELECT_ALL,
        DELETE
    }
}