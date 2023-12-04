package site.pnpl.mira.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import site.pnpl.mira.data.dao.CheckInDao
import site.pnpl.mira.data.entity.CheckIn
import site.pnpl.mira.utils.MiraDateFormat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CheckInRepository(private val checkInDao: CheckInDao) {

    suspend fun insertCheckIn(checkIn: CheckIn) {
        return withContext(Dispatchers.IO) {
            checkInDao.insert(checkIn)
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

    suspend fun getCheckInForPeriod(startPeriod: Long, endPeriod: Long): List<CheckIn> =
        suspendCoroutine { continuation ->
            continuation.resume(
                checkInDao.getByPeriod(
                    MiraDateFormat(startPeriod).getFormatForBD(),
                    MiraDateFormat(endPeriod).getFormatForBD()
                )
            )
        }


    suspend fun insertListOfCheckIns(list: List<CheckIn>) {
        withContext(Dispatchers.IO) {
            checkInDao.insertListOfCheckIns(list)
        }
    }

    suspend fun deleteListOfCheckIns(checkIns: List<CheckIn>) {
        withContext(Dispatchers.IO) {
            checkInDao.deleteListOfCheckIns(checkIns)
        }
    }

}