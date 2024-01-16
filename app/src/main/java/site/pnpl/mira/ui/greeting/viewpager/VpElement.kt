package site.pnpl.mira.ui.greeting.viewpager

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VpElement(
    val animation: String,
    val title: String,
    val text: String
) : Parcelable