package site.pnpl.mira.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import site.pnpl.mira.data.dao.CheckInDao
import site.pnpl.mira.data.entity.CheckIn
import site.pnpl.mira.utils.MiraDateFormat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CheckInRepository(private val checkInDao: CheckInDao) {

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

    suspend fun getCheckInForPeriod(startPeriod: Long, endPeriod: Long): List<CheckIn> {
        val list = suspendCoroutine { continuation ->
            continuation.resume(
                checkInDao.getByPeriod(
                    MiraDateFormat(startPeriod).getFormatForBD(),
                    MiraDateFormat(endPeriod).getFormatForBD()
                )
            )
        }
        println("startPeriod ${MiraDateFormat(startPeriod).getFormatForBD()} endPeriod ${MiraDateFormat(endPeriod).getFormatForBD()}")
        println("list.size ${list.size} $list")
        return list
    }

    suspend fun insertListOfCheckIns(list: List<CheckIn>) {
        withContext(Dispatchers.IO) {
            checkInDao.insertListOfCheckIns(list)
        }
    }
}