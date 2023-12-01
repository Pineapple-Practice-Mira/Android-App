package site.pnpl.mira.ui.home.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import site.pnpl.mira.R
import site.pnpl.mira.databinding.ActionBarBinding
import site.pnpl.mira.utils.MiraDateFormat
import java.util.Calendar

class ActionBar(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    private var _binding: ActionBarBinding? = null
    private val binding: ActionBarBinding get() = _binding!!

    private lateinit var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>

    var currentPeriod: Pair<Long, Long>? = null
        private set

    init {
        _binding = ActionBarBinding.bind(LayoutInflater.from(context).inflate(R.layout.action_bar, this))
    }

    fun initDatePicker(startPeriod: Long, endPeriod: Long) {
        val endConstraint = System.currentTimeMillis()
        val startConstraint = Calendar.getInstance().apply {
            timeInMillis = endConstraint
            add(Calendar.MONTH, -3)
        }.timeInMillis

        dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(resources.getString(R.string.calendar_tittle))
            .setPositiveButtonText(resources.getString(R.string.calendar_positive_button))
            .setNegativeButtonText(resources.getString(R.string.calendar_negative_button))
            .setTheme(R.style.MiraDatePicker)
            .setSelection(
                Pair(
                    startPeriod,
                    endPeriod
                )
            )
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setStart(startConstraint)
                    .setEnd(endConstraint)
                    .build()
            )
            .build()
    }

    fun setActionBarClickListener(listener: (Button) -> Unit) {
        with(binding) {
            statistic.setOnClickListener { listener(Button.STATISTIC) }
            selector.setOnClickListener {
                selector.isSelected = !selector.isSelected
                listener(Button.SELECT_ALL)
            }
            delete.setOnClickListener { listener(Button.DELETE) }
        }
    }

    fun setCalendarPeriodSelectionListener(childFragmentManager: FragmentManager, listener: (Pair<Long, Long>) -> Unit) {
        binding.calendar.setOnClickListener {
            dateRangePicker.show(childFragmentManager, DATE_PICKER_TAG)

            dateRangePicker.addOnPositiveButtonClickListener { periods ->
                setSelectedPeriod(periods)
                listener(periods)
            }

        }

    }

    fun setSelectedPeriod(periods: Pair<Long, Long>) {
        val dateFirst = MiraDateFormat(periods.first)
        val dateSecond = MiraDateFormat(periods.second)
        val periodString =
            "${dateFirst.getNameMonthUpper()} ${dateFirst.getDateOfMonth()} - ${dateSecond.getNameMonthUpper()} ${dateSecond.getDateOfMonth()}"
        binding.period.text = periodString
        currentPeriod = periods
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

    fun enableActionBarButtons(value: Boolean) {
        binding.calendar.isEnabled = value
    }

    fun trashBoxEnable(value: Boolean) {
        binding.delete.isEnabled = value
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

    companion object {
        const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
    }

    enum class Button {
        CALENDAR,
        STATISTIC,
        SELECT_ALL,
        DELETE
    }
}