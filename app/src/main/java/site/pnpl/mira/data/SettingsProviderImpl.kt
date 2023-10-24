package site.pnpl.mira.data

import android.content.Context
import javax.inject.Inject

class SettingsProviderImpl @Inject constructor(context: Context) : SettingsProvider {

    private val preferenceFileName = "settings"
    private val preferenceFieldName = "name"
    private val preferenceFieldFirstLaunch = "first_launch"

    private val preference = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE)
    override fun saveName(name: String) {
        preference.edit().apply {
            putString(preferenceFieldName, name)
            apply()
        }
    }

    override fun getName(): String = preference.getString(preferenceFieldName, "")!!

    override fun isFirstLaunch(): Boolean = preference.getBoolean(preferenceFieldFirstLaunch, true)

    override fun firstLaunchCompleted() {
        preference.edit().apply {
            putBoolean(preferenceFieldFirstLaunch, false)
            apply()
        }
    }

}