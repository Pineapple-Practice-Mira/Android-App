package site.pnpl.mira.ui.greeting.viewpager

import site.pnpl.mira.R

object VpElementsGenerator {
    fun generate() = listOf<VpElement>(
        VpElement(R.raw.mira, R.string.acquaintance_title_1, R.string.acquaintance_text_1),
        VpElement(R.raw.mira, R.string.acquaintance_title_2, R.string.acquaintance_text_2),
        VpElement(R.raw.mira, R.string.acquaintance_title_3, R.string.acquaintance_text_3),
        VpElement(R.raw.mira, R.string.acquaintance_title_4, R.string.acquaintance_text_4),
        VpElement(R.raw.mira, R.string.acquaintance_title_5, R.string.acquaintance_text_5),
        VpElement(R.raw.mira, R.string.acquaintance_title_6, R.string.acquaintance_text_6),
    )
}