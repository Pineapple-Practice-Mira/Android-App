package site.pnpl.mira.domain.analitycs

import android.content.Context
import android.os.Build
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject
import site.pnpl.mira.BuildConfig
import javax.inject.Inject


class MixPanelAnalytics @Inject constructor(
    applicationContext: Context
) : Analytics {

    private val mixpanelAPI: MixpanelAPI =
        MixpanelAPI.getInstance(applicationContext, MixPanel.TOKEN, !BuildConfig.DEBUG)

    override fun sendEvent(@AnalyticsEvent.Name eventName: String, params: List<EventParameter>) {
        if (!BuildConfig.DEBUG) {
            val props = JSONObject()
            if (params.isNotEmpty()) {
                params.forEach { parameter ->
                    props.put(parameter.key, parameter.value)
                }
            }
            mixpanelAPI.track(eventName, props)
        }
    }

}