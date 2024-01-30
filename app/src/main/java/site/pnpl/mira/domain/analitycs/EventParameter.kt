package site.pnpl.mira.domain.analitycs

data class EventParameter(
    @AnalyticsEvent.Parameter val key: String,
    val value: Any?
)
