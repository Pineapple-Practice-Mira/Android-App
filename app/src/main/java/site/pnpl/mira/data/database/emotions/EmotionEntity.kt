package site.pnpl.mira.data.database.emotions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import site.pnpl.mira.data.database.DBConstants

@Entity(
    tableName = DBConstants.TABLE_NAME_EMOTION,
    indices = [Index(
        value = ["emotion_id"],
        unique = true
    )]
)
data class EmotionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "emotion_id") val emotionId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "name_genitive") val nameGenitive: String,
    @ColumnInfo(name = "remote_emoji_link") val remoteEmojiLink: String,
    @ColumnInfo(name = "local_emoji_link") val localEmojiLink: String,
    @ColumnInfo(name = "is_positive") val isPositive: Int,
    @ColumnInfo(name = "is_opened") val isOpened: Int
)