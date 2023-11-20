package site.pnpl.mira.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.core.util.Pair
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
            selector.setOnClickListener { listener(Button.SELECT_ALL) }
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