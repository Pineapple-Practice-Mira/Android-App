package site.pnpl.mira.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class FactorData(
    val factorId: Int,
    @StringRes val nameIdRes: Int,
    var positiveCount: Int = 0,
    var negativeCount: Int = 0,
    var totalCount: Int = 0
) : Parcelable