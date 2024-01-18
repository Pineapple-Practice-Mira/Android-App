package site.pnpl.mira.data.database.exercises

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import site.pnpl.mira.data.database.DBConstants

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exerciseEntity: ExerciseEntity)

    @Query("SELECT * FROM ${DBConstants.TABLE_NAME_EXERCISES} WHERE exercise_id LIKE :exerciseId")
    fun getExerciseById(exerciseId: Int): ExerciseEntity?
}