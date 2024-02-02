package site.pnpl.mira.domain.analitycs

import androidx.annotation.StringDef

class AnalyticsEvent {

    @StringDef(
        value = [
            NAME_ACTION_BAR_CALENDAR_OPEN,
            NAME_ACTION_BAR_DELETE_CHECK_INS,
            NAME_ACTION_BAR_STATISTIC,
            NAME_BOTTOM_BAR_CLICK_CHECK_IN,
            NAME_BOTTOM_BAR_CLICK_EXERCISE_LIST,
            NAME_CHECK_IN_COMPLETE,
            NAME_CHECK_IN_EXERCISE_CLICK,
            NAME_CHECK_IN_EXERCISE_REFRESH_CLICK,
            NAME_CHECK_IN_FACTOR_CLOSE,
            NAME_CHECK_IN_FACTOR_CLOSE_NO,
            NAME_CHECK_IN_FACTOR_CLOSE_YES,
            NAME_CHECK_IN_FACTOR_RETURN,
            NAME_CHECK_IN_FEELING_CLOSE,
            NAME_CHECK_IN_FEELING_CLOSE_NO,
            NAME_CHECK_IN_FEELING_CLOSE_YES,
            NAME_CHECK_IN_FEELING_FINISH,
            NAME_CHECK_IN_SAVED_CLOSE,
            NAME_CHECK_IN_SAVED_CLOSE_VIA_BUTTON,
            NAME_EXERCISES_LIST_CLICK_INTRO,
            NAME_EXERCISES_LIST_FIRST_CHECK_IN,
            NAME_EXERCISES_LIST_OPEN_EXERCISE,
            NAME_EXERCISE_CLOSE,
            NAME_EXERCISE_PREVIEW_CLOSE,
            NAME_EXERCISE_PROGRESS,
            NAME_EXERCISE_START,
            NAME_GREETING_COMPLETE,
            NAME_GREETING_COMPLETE_VIA_BUTTON,
            NAME_GREETING_PROGRESS,
            NAME_GREETING_SKIP,
            NAME_GREETING_START,
            NAME_INTER_NAME_CONFIRM,
            NAME_INTER_NAME_SKIP,
            NAME_REQUEST_ERROR,
            NAME_REQUEST_SUCCESS,
            NAME_SETTINGS_ABOUT,
            NAME_SETTINGS_CLOSE,
            NAME_SETTINGS_OPEN,
            NAME_SETTINGS_SAVE_NAME,
            NAME_SETTINGS_SHARE,
        ]
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Name

    @StringDef(
        value = [
            PARAMETER_EMOTION_ID,
            PARAMETER_EMOTION_NAME,
            PARAMETER_EXERCISE_ID,
            PARAMETER_EXERCISE_NAME,
            PARAMETER_SKIP,
            PARAMETER_STEP,
            PARAMETER_REQUEST_ERROR_BODY,
            PARAMETER_REQUEST_URL,
            PARAMETER_RESPONSE_TIME
        ]
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Parameter

    companion object {
        const val NAME_ACTION_BAR_CALENDAR_OPEN = "calendar"
        const val NAME_ACTION_BAR_DELETE_CHECK_INS = "checkin_delete"
        const val NAME_ACTION_BAR_STATISTIC = "statistics"
        const val NAME_BOTTOM_BAR_CLICK_CHECK_IN = "checkin_start"
        const val NAME_BOTTOM_BAR_CLICK_EXERCISE_LIST = "workout_mainscreen"
        const val NAME_CHECK_IN_COMPLETE = "checkin_finish"
        const val NAME_CHECK_IN_EXERCISE_CLICK = "workout_start"
        const val NAME_CHECK_IN_EXERCISE_REFRESH_CLICK = "workout_error"
        const val NAME_CHECK_IN_FACTOR_CLOSE = "checkin_factor_cancel"
        const val NAME_CHECK_IN_FACTOR_CLOSE_NO = "checkin_factor_cancel_no"
        const val NAME_CHECK_IN_FACTOR_CLOSE_YES = "checkin_factor_cancel_yes"
        const val NAME_CHECK_IN_FACTOR_RETURN = "checkin_factor_return"
        const val NAME_CHECK_IN_FEELING_CLOSE = "checkin_feeling_cancel"
        const val NAME_CHECK_IN_FEELING_CLOSE_NO = "checkin_feeling_cancel_no"
        const val NAME_CHECK_IN_FEELING_CLOSE_YES = "checkin_feeling_cancel_yes"
        const val NAME_CHECK_IN_FEELING_FINISH = "checkin_feeling_finish"
        const val NAME_CHECK_IN_SAVED_CLOSE = "checkin_close"
        const val NAME_CHECK_IN_SAVED_CLOSE_VIA_BUTTON = "workout_close"
        const val NAME_EXERCISES_LIST_CLICK_INTRO = "workout_onboarding"
        const val NAME_EXERCISES_LIST_FIRST_CHECK_IN = "workout_checkin"
        const val NAME_EXERCISES_LIST_OPEN_EXERCISE = "workout_go"
        const val NAME_EXERCISE_CLOSE = "workout_finish"
        const val NAME_EXERCISE_PREVIEW_CLOSE = "workout_button_close"
        const val NAME_EXERCISE_PROGRESS = "workout"
        const val NAME_EXERCISE_START = "workout_button_start"
        const val NAME_GREETING_COMPLETE = "onboarding_finish"
        const val NAME_GREETING_COMPLETE_VIA_BUTTON = "onboarding_finish_via_button"
        const val NAME_GREETING_PROGRESS = "onboarding"
        const val NAME_GREETING_SKIP = "onboarding_skip"
        const val NAME_GREETING_START = "onboarding_start"
        const val NAME_INTER_NAME_CONFIRM = "name_confirm"
        const val NAME_INTER_NAME_SKIP = "name_skip"
        const val NAME_REQUEST_ERROR = "request_error"
        const val NAME_REQUEST_SUCCESS = "request_success"
        const val NAME_SETTINGS_ABOUT = "settings_project"
        const val NAME_SETTINGS_CLOSE = "settings_button_cross"
        const val NAME_SETTINGS_OPEN = "settings"
        const val NAME_SETTINGS_SAVE_NAME = "settings_name_save"
        const val NAME_SETTINGS_SHARE = "settings_share"

        const val PARAMETER_EMOTION_ID = "emotion_id"
        const val PARAMETER_EMOTION_NAME = "emotion_name"
        const val PARAMETER_EXERCISE_ID = "exercise_id"
        const val PARAMETER_EXERCISE_NAME = "exercise_name"
        const val PARAMETER_REQUEST_ERROR_BODY = "request_error_body"
        const val PARAMETER_REQUEST_URL = "request_url"
        const val PARAMETER_RESPONSE_TIME = "response_time"
        const val PARAMETER_SKIP = "skip"
        const val PARAMETER_STEP = "step"
    }
}

