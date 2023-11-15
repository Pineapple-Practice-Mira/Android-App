package site.pnpl.mira.data

import androidx.room.Database
import androidx.room.RoomDatabase
import site.pnpl.mira.data.dao.CheckInDao
import site.pnpl.mira.data.entity.CheckIn


@Database(
    entities = [
        CheckIn::class
    ],
    version = DBConstants.VERSION,
    exportSchema = false
)
abstract class MiraDatabase : RoomDatabase() {
    abstract fun checkInDao(): CheckInDao
}