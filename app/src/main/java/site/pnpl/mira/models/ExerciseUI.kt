package site.pnpl.mira.models

data class ExerciseUI(
    val id: Int,
    val name: String,
    val title: String,
    val description: String,
    val previewImageLink: String,
    val emotionsId: List<Int>,
    val screens: List<ScreenUI>,
)