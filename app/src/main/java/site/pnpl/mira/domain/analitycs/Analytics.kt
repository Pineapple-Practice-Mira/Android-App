package site.pnpl.mira.domain.analitycs

interface Analytics {
    fun sendEvent(@AnalyticsEvent.Name eventName: String, params: List<EventParameter> = emptyList())
}

