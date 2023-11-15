package site.pnpl.mira.data

import android.content.Context
import javax.inject.Inject

class SettingsProviderImpl @Inject constructor(context: Context) : SettingsProvider {


    private val preference = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    override fun saveName(name: String) {
        preference.edit().apply {
            putString(FIELD_AT_NAME, name)
            apply()
        }
    }

    override fun getName(): String = preference.getString(FIELD_AT_NAME, "")!!

    override fun isFirstLaunch(): Boolean = preference.getBoolean(FILED_AT_FIRST_LAUNCH, true)

    override fun firstLaunchCompleted() {
        preference.edit().apply {
            putBoolean(FILED_AT_FIRST_LAUNCH, false)
            apply()
        }
    }

    companion object {
        private const val FILE_NAME = "settings"
        private const val FIELD_AT_NAME = "name"
        private const val FILED_AT_FIRST_LAUNCH = "first_launch"
    }

}