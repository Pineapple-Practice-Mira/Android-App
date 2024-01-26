package site.pnpl.mira.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseUI(
    val id: Int,
    val name: String,
    val description: String,
    val previewImageLink: String,
    val emotionsId: List<Int>,
    val screens: List<ScreenUI>,
    val isIntro: Boolean = false,
    val isOpened: Boolean = false
) : Parcelable