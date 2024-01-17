package site.pnpl.mira.data.database.exercises

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import site.pnpl.mira.data.database.DBConstants.TABLE_NAME_EXERCISES

@Entity(
    tableName = TABLE_NAME_EXERCISES,
    indices = [Index(
        value = ["exercise_id"],
        unique = true
    )]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "exercise_id") val exerciseId: Int,
    @ColumnInfo(name = "is_opened") val isOpened: Int
)