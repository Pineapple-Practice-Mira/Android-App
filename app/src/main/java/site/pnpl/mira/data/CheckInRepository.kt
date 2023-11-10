package site.pnpl.mira.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import site.pnpl.mira.data.dao.CheckInDao
import site.pnpl.mira.data.entity.CheckIn

class CheckInRepository(private val checkInDao: CheckInDao) {

    val checkInFlow: Flow<List<CheckIn>>
        get() = checkInDao.getAllCheckIn()

    suspend fun insertCheckIn(checkIn: CheckIn): Boolean {
        return withContext(Dispatchers.IO) {
            checkInDao.insert(checkIn)
            return@withContext true
        }
    }

    suspend fun getAllCheckIns() {
        return withContext(Dispatchers.IO) {
            checkInDao.getAllCheckIn()
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            checkInDao.deleteAll()
        }
    }
}