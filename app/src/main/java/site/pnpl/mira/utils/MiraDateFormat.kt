package site.pnpl.mira.utils

import android.text.format.DateFormat
import java.util.Calendar
import java.util.Locale

class MiraDateFormat(millis: Long) {

    private val calendar: Calendar = Calendar.Builder()
        .setInstant(millis)
        .build()


    fun getNameDayOfWeek(): String =
        DateFormat.format("E", calendar.time).toString()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    fun getDateOfMonth(): String =
        DateFormat.format("d", calendar.time).toString()

    fun getNameMonth(): String =
        DateFormat.format("MMM", calendar.time).toString()
            .split(".")[0]

    fun getTime(): String =
        DateFormat.format("HH:mm", calendar.time).toString()

    fun getDayOfWeekAndTime(): String =
        "${getNameDayOfWeek()}, ${getTime()}"
}