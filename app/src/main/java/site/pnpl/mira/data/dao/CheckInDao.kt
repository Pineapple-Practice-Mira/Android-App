package site.pnpl.mira.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import site.pnpl.mira.data.DBConstants
import site.pnpl.mira.data.entity.CheckIn

@Dao
interface CheckInDao {
    @Query("SELECT * FROM ${DBConstants.TABLE_NAME_CHECK_IN}")
    fun getAllCheckIn(): Flow<List<CheckIn>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(checkIn: CheckIn)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertListOfCheckIns(list: List<CheckIn>)

    @Query("DELETE FROM ${DBConstants.TABLE_NAME_CHECK_IN}")
    fun deleteAll()

    @Query("SELECT * FROM ${DBConstants.TABLE_NAME_CHECK_IN} WHERE date(created_at) BETWEEN :startPeriod AND :endPeriod")
    fun getForPeriod(startPeriod: String, endPeriod: String): List<CheckIn>

    @Delete
    fun deleteListOfCheckIns(checkIns: List<CheckIn>)

    @Query("SELECT COUNT(*) FROM ${DBConstants.TABLE_NAME_CHECK_IN}")
    fun getCountCheckIns(): Long

    @Query("SELECT COUNT(factor_id) FROM ${DBConstants.TABLE_NAME_CHECK_IN} WHERE factor_id LIKE :factorId")
    fun getCountCheckInsByFactor(factorId: Int): Long

    @Query("SELECT * FROM ${DBConstants.TABLE_NAME_CHECK_IN} " +
            "WHERE (date(created_at) BETWEEN :startPeriod AND :endPeriod) " +
            "AND factor_id LIKE :factorId")
    fun getForPeriodByFactorId(startPeriod: String, endPeriod: String, factorId: Int): List<CheckIn>
}