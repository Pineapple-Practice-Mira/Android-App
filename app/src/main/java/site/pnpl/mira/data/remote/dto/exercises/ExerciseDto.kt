package site.pnpl.mira.data.remote.dto.exercises


import com.google.gson.annotations.SerializedName
import site.pnpl.mira.data.remote.dto.emotions.EmotionDtoItem


class ExerciseDtoList : ArrayList<ExerciseDto>()

data class ExerciseDto(
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
    @SerializedName("emotions")
    val emotions: List<EmotionDtoItem>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_intro")
    val isIntro: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("preview_image_link")
    val previewImageLink: String,
    @SerializedName("published")
    val published: Boolean,
    @SerializedName("screens")
    val screens: List<ScreenDto>?,
    @SerializedName("title")
    val title: String?
)