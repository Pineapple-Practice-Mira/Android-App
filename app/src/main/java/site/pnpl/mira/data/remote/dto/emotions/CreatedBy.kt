package site.pnpl.mira.data.remote.dto.emotions


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CreatedBy(
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("username")
    val username: String
)