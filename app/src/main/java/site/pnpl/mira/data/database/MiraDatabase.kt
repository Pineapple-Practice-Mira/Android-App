package site.pnpl.mira.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import site.pnpl.mira.data.database.check_in.dao.CheckInDao
import site.pnpl.mira.data.database.check_in.entity.CheckIn
import site.pnpl.mira.data.database.emotions.EmotionDao
import site.pnpl.mira.data.database.emotions.EmotionEntity
import site.pnpl.mira.data.database.exercises.ExerciseDao
import site.pnpl.mira.data.database.exercises.ExerciseEntity


@Database(
    entities = [
        CheckIn::class,
        EmotionEntity::class,
        ExerciseEntity::class
    ],
    version = DBConstants.VERSION,
    exportSchema = false
)
abstract class MiraDatabase : RoomDatabase() {
    abstract fun checkInDao(): CheckInDao
    abstract fun emotionDao(): EmotionDao
    abstract fun exerciseDao(): ExerciseDao
}