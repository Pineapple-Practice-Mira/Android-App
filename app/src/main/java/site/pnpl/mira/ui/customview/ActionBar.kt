package site.pnpl.mira.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.databinding.ActionBarBinding
import site.pnpl.mira.domain.SettingsProvider
import site.pnpl.mira.domain.analitycs.Analytics
import site.pnpl.mira.domain.analitycs.AnalyticsEvent
import site.pnpl.mira.utils.MiraDateFormat
import javax.inject.Inject

class ActionBar(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    private var _binding: ActionBarBinding? = null
    private val binding: ActionBarBinding get() = _binding!!

    private lateinit var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>

    @Inject
    lateinit var analytics: Analytics

    @Inject
    lateinit var settingsProvider: SettingsProvider

    init {
        _binding = ActionBarBinding.bind(LayoutInflater.from(context).inflate(R.layout.action_bar, this))
        App.instance.appComponent.inject(this)
    }

    fun initDatePicker(startPeriod: Long, endPeriod: Long) {
        val endConstraint = System.currentTimeMillis()

        val startConstraint = settingsProvider.getFirstStartMonth()

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
        setSelectedPeriod(Pair(startPeriod, endPeriod))
    }

    fun setDisplayMode(mode: DisplayMode) {
        when (mode) {
            DisplayMode.HOME -> {}
            DisplayMode.STATISTIC_FIRST -> {
                binding.statistic.setColorFilter(ContextCompat.getColor(context, R.color.secondary))
                binding.statistic.isClickable = false
            }

            DisplayMode.STATISTIC_SECOND -> {
                binding.calendar.isEnabled = false
                binding.statistic.setColorFilter(ContextCompat.getColor(context, R.color.secondary))
                binding.statistic.isClickable = false
            }
        }
    }

    fun setActionBarClickListener(listener: (Button) -> Unit) {
        with(binding) {
            statistic.setOnClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_ACTION_BAR_STATISTIC)
                listener(Button.STATISTIC)
            }
            selector.setOnClickListener {
                selector.isSelected = !selector.isSelected
                listener(Button.SELECT_ALL)
            }
            delete.setOnClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_ACTION_BAR_DELETE_CHECK_INS)
                listener(Button.DELETE)
            }
        }
    }

    fun selectSelector(value: Boolean) {
        binding.selector.isSelected = value
    }

    fun setCalendarPeriodSelectionListener(childFragmentManager: FragmentManager, listener: (Pair<Long, Long>) -> Unit) {
        binding.calendar.setOnClickListener {
            analytics.sendEvent(AnalyticsEvent.NAME_ACTION_BAR_CALENDAR_OPEN)

            dateRangePicker.show(childFragmentManager, DATE_PICKER_TAG)

            dateRangePicker.addOnPositiveButtonClickListener { periods ->
                setSelectedPeriod(periods)
                listener(periods)
            }
        }
    }

    private fun setSelectedPeriod(periods: Pair<Long, Long>) {
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

            if (value) {
                changeAlphaAnimation(actionBarDateLabel, REMOVE_MODE_ACTIVE_ALPHA)
                changeAlphaAnimation(period, REMOVE_MODE_ACTIVE_ALPHA)
            } else {
                changeAlphaAnimation(actionBarDateLabel, REMOVE_MODE_INACTIVE_ALPHA)
                changeAlphaAnimation(period, REMOVE_MODE_INACTIVE_ALPHA)
            }
        }
        if (!value) {
            binding.selector.isSelected = false
        }
    }

    private fun changeAlphaAnimation(view: View, targetAlpha: Float) {
        view.animate()
            .alpha(targetAlpha)
            .setDuration(REMOVE_MODE_CHANGE_ALPHA_DURATION)
            .start()
    }

    fun enableActionBarButtons(value: Boolean) {
        binding.calendar.isEnabled = value
        binding.statistic.isEnabled = value
    }

    fun deleteButtonEnable(value: Boolean) {
        binding.delete.isEnabled = value
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

    companion object {
        const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
        const val REMOVE_MODE_CHANGE_ALPHA_DURATION = 300L
        const val REMOVE_MODE_ACTIVE_ALPHA = 0.3f
        const val REMOVE_MODE_INACTIVE_ALPHA = 1f
    }

    enum class Button {
        CALENDAR,
        STATISTIC,
        SELECT_ALL,
        DELETE
    }

    enum class DisplayMode {
        HOME,
        STATISTIC_FIRST,
        STATISTIC_SECOND
    }
}