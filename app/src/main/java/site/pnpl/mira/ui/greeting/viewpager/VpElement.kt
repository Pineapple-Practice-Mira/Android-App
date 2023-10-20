package site.pnpl.mira.ui.greeting.viewpager

import android.os.Parcelable
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class VpElement(
    @RawRes val animation: Int,
    @StringRes val title: Int,
    @StringRes val text: Int
) : Parcelable