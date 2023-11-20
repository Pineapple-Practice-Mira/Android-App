package site.pnpl.mira.model

import site.pnpl.mira.data.entity.CheckIn


data class CheckInUI (
    val id: Int,
    val emotionId: Int,
    val factorId: Int,
    val exercisesId: Int = 0,
    val note: String = "",
    val createdAt: String,
    val createdAtLong: Long,
    val editedAt: String = "",
    val isSynchronized: Boolean = false,
    var isSelected: Boolean
)

fun CheckInUI.mapToCheckIn(): CheckIn {
    return CheckIn(
        id = id,
        emotionId = emotionId,
        factorId = factorId,
        exercisesId = exercisesId,
        note = note,
        createdAt = createdAt,
        createdAtLong = createdAtLong,
        editedAt = editedAt,
        isSynchronized = if (isSynchronized) 1 else 0
    )
}