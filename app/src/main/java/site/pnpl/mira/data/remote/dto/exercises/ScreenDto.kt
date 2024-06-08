package site.pnpl.mira.data.remote.dto.exercises


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ScreenDto(
    @SerializedName("animation_link")
    val animationLink: String,
    @SerializedName("sequence_number")
    val sequenceNumber: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("title")
    val title: String
)