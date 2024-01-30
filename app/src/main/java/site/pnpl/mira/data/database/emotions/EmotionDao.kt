package site.pnpl.mira.data.database.emotions

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import site.pnpl.mira.data.database.DBConstants

@Dao
interface EmotionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmotions(emotions: List<EmotionEntity>)

    @Delete()
    fun deleteEmotions(emotions: List<EmotionEntity>)

    @Query("SELECT * FROM ${DBConstants.TABLE_NAME_EMOTION}")
    fun getAllEmotions(): List<EmotionEntity>

//    @Query("SELECT name FROM ${DBConstants.TABLE_NAME_EMOTION} WHERE emotion_id LIKE :id")
//    fun getEmotionNameById(id: Int): String
//
//    @Query("SELECT name_genitive FROM ${DBConstants.TABLE_NAME_EMOTION} WHERE emotion_id LIKE :id")
//    fun getEmotionNameParentCaseById(id: Int): String
//
//    @Query("SELECT is_positive FROM ${DBConstants.TABLE_NAME_EMOTION} WHERE emotion_id LIKE :id")
//    fun getPositiveFlag(id: Int): Int
//
//    @Query("SELECT is_opened FROM ${DBConstants.TABLE_NAME_EMOTION} WHERE emotion_id LIKE :id")
//    fun getOpenedFlagById(id: Int): Int

    @Query("UPDATE emotion_table SET is_opened=:flag WHERE id = :emotionId")
    fun updateOpenedFlag(emotionId: Int, flag: Boolean)
}