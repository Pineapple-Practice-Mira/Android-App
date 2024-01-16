package site.pnpl.mira.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScreenUI(
    val animationLink: String,
    val sequenceNumber: Int,
    val text: String,
    val title: String
) : Parcelable