package site.pnpl.mira.utils

import android.text.format.DateFormat
import java.util.Calendar
import java.util.Locale

class MiraDateFormat(millis: Long) {

    private val calendar: Calendar = Calendar.Builder()
        .setInstant(millis)
        .build()


    /**
     * @return example format: Пн
     */
    fun getNameDayOfWeek(): String =
        DateFormat.format("E", calendar.time).toString()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    /**
     * @return example format: 15
     */
    fun getDateOfMonth(): String =
        DateFormat.format("dd", calendar.time).toString()

    /**
     * @return example format: нояб
     */
    fun getNameMonth(): String =
        DateFormat.format("MMM", calendar.time).toString()
            .split(".")[0]

    /**
     * @return example format: 23:12
     */
    fun getTime(): String =
        DateFormat.format("HH:mm", calendar.time).toString()

    /**
     * @return example format: Пн, 23:12
     */
    fun getDayOfWeekAndTime(): String =
        "${getNameDayOfWeek()}, ${getTime()}"

    /**
     * @return example format: 21 сентября 2023
     */
    fun getDayMonthYear(): String =
        DateFormat.format("dd MMMM yyyy", calendar.time).toString()
}