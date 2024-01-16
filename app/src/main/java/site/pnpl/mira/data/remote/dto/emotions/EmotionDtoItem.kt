package site.pnpl.mira.data.remote.dto.emotions


import com.google.gson.annotations.SerializedName

data class EmotionDtoItem(
    @SerializedName("name")
    val name: String,
    @SerializedName("name_genitive")
    val nameGenitive: String,
    @SerializedName("emoji_link")
    val emojiLink: String,
    @SerializedName("is_positive")
    val isPositive: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: CreatedBy,
    @SerializedName("description")
    val description: String,
    @SerializedName("edited_at")
    val editedAt: String,
    @SerializedName("edited_by")
    val editedBy: EditedBy,
    @SerializedName("exercises")
    val exercises: List<Exercise>,
    @SerializedName("id")
    val id: Int
)