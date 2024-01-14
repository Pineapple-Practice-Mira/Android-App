package site.pnpl.mira.data

data class EmotionDataModel(
    val emotionId: Int,
    val name: String,
    val nameGenitive: String,
    val remoteEmojiLink: String,
    val localEmojiLink: String,
    val isPositive: Boolean,
    val isOpened: Boolean
) {

    override fun hashCode(): Int {
        var result = emotionId
        result = 31 * result + name.hashCode()
        result = 31 * result + nameGenitive.hashCode()
        result = 31 * result + remoteEmojiLink.hashCode()
        result = 31 * result + isPositive.hashCode()
        result = 31 * result + isOpened.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmotionDataModel

        if (emotionId != other.emotionId) return false
        if (name != other.name) return false
        if (nameGenitive != other.nameGenitive) return false
        if (remoteEmojiLink != other.remoteEmojiLink) return false
        if (isPositive != other.isPositive) return false
        if (isOpened != other.isOpened) return false

        return true
    }
}