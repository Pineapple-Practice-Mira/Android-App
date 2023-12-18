package site.pnpl.mira.ui.greeting.viewpager

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class VpElement(
    val animation: Int,
    @StringRes val title: Int,
    @StringRes val text: Int
) : Parcelable