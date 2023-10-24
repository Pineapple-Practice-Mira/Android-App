package site.pnpl.mira.data

interface SettingsProvider {
    fun saveName(name: String)

    fun getName(): String

    fun isFirstLaunch() : Boolean

    fun firstLaunchCompleted()
}