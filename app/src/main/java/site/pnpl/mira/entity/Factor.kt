package site.pnpl.mira.entity

import androidx.annotation.StringRes
import site.pnpl.mira.R

data class Factor(
    val id: Int,
    @StringRes val nameResId: Int
)

object FactorsList {
    val factors = listOf(
        Factor(id = 0, nameResId = R.string.house),
        Factor(id = 1, nameResId = R.string.job),
        Factor(id = 2, nameResId = R.string.family),
        Factor(id = 3, nameResId = R.string.partner),
        Factor(id = 4, nameResId = R.string.society),
        Factor(id = 5, nameResId = R.string.studies),
        Factor(id = 6, nameResId = R.string.friends),
        Factor(id = 7, nameResId = R.string.rest),
        Factor(id = 8, nameResId = R.string.hobby),
        Factor(id = 9, nameResId = R.string.sport),
        Factor(id = 10, nameResId = R.string.health),
        Factor(id = 11, nameResId = R.string.creation),
    )
}