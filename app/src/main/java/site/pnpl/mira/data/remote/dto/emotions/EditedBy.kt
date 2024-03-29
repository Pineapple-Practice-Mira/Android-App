package site.pnpl.mira.data.remote.dto.emotions


import com.google.gson.annotations.SerializedName

data class EditedBy(
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("username")
    val username: String
)