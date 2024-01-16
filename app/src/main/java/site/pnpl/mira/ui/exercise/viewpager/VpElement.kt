package site.pnpl.mira.ui.exercise.viewpager

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VpElement(
    val animation: String,
    val title: String,
    val text: String
) : Parcelable