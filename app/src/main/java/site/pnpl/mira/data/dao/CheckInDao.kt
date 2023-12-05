package site.pnpl.mira.data.dao

import androidx.paging.PagingSource
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
    fun getByPeriod(startPeriod: String, endPeriod: String): List<CheckIn>
    @Delete
    fun deleteListOfCheckIns(checkIns: List<CheckIn>)

    @Query("SELECT * FROM ${DBConstants.TABLE_NAME_CHECK_IN} WHERE date(created_at) BETWEEN :startPeriod AND :endPeriod ORDER BY created_at_long DESC")
    fun getByPeriodP(startPeriod: String, endPeriod: String): PagingSource<Int, CheckIn>

    @Query("SELECT * FROM ${DBConstants.TABLE_NAME_CHECK_IN} ORDER BY created_at DESC")
    fun getAllCheckInP(): PagingSource<Int, CheckIn>

}