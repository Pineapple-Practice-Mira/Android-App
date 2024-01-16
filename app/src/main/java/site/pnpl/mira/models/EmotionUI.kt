package site.pnpl.mira.models

data class EmotionUI(
    val id: Int,
    val name: String,
    val nameGenitive: String,
    val isPositive: Boolean,
    val emojiLink: String,
    var isOpened: Boolean
)