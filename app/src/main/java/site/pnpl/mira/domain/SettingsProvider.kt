package site.pnpl.mira.domain

interface SettingsProvider {
    fun saveName(name: String)

    fun getName(): String

    fun isFirstLaunch(): Boolean

    fun firstLaunchCompleted()

    fun isMakeFirstCheckIn(): Boolean

    fun firstCheckInCreated()

    fun saveFirstStartMonth(millis: Long)
    fun getFirstStartMonth(): Long
}