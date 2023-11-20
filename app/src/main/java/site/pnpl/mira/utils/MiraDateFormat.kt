package site.pnpl.mira.utils

import android.text.format.DateFormat
import java.util.Locale

class MiraDateFormat(val millis: Long) {

    /**
     * @return example format: Пн
     */
    fun getNameDayOfWeek(): String =
        DateFormat.format("E", millis).toString()
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }

    /**
     * @return example format: 15
     */
    fun getDateOfMonth(): String =
        DateFormat.format("dd", millis).toString()

    /**
     * @return example format: нояб
     */
    fun getNameMonth(): String =
        DateFormat.format("MMM", millis).toString()
            .split(".")[0]

    fun getNameMonthUpper(): String =
        DateFormat.format("MMM", millis).toString()
            .split(".")[0]
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }

    /**
     * @return example format: 23:12
     */
    fun getTime(): String =
        DateFormat.format("HH:mm", millis).toString()

    /**
     * @return example format: Пн, 23:12
     */
    fun getDayOfWeekAndTime(): String =
        "${getNameDayOfWeek()}, ${getTime()}"

    /**
     * @return example format: 21 сентября 2023
     */
    fun getDayMonthYear(): String =
        DateFormat.format("dd MMMM yyyy", millis).toString()

    fun getFormatForBD(): String =
        DateFormat.format("yyyy-MM-dd", millis).toString()

    fun convertToDataTimeISO8601(): String {
        return DateFormat.format("yyyy-MM-dd'T'HH:mm:ss", millis).toString()
    }
}