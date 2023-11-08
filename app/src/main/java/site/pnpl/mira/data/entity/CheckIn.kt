package site.pnpl.mira.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import site.pnpl.mira.data.DBConstants

@Parcelize
@Entity(
    tableName = DBConstants.TABLE_NAME_CHECK_IN,
    indices = [Index(
        value = ["created_at"],
        unique = true
    )]
)
data class CheckIn(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "emotion_id") val emotionId: Int,
    @ColumnInfo(name = "factor_id") val factorId: Int,
    @ColumnInfo(name = "exercises_id") val exercisesId: Int = 0,
    @ColumnInfo(name = "note") val note: String = "",
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "edited_at") val editedAt: String = "",
    @ColumnInfo(name = "is_synchronized", defaultValue = "0") val isSynchronized: Int = 0
) : Parcelable