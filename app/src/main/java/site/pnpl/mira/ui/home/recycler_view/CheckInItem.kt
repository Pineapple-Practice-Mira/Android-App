package site.pnpl.mira.ui.home.recycler_view

import android.graphics.drawable.Drawable

data class CheckInItem(
    val day: String,
    val month: String,
    val dayOfWeekAndTime: String,
    val emotion: String,
    val emotionDrawable: Drawable,
    val dateTime:Long
)