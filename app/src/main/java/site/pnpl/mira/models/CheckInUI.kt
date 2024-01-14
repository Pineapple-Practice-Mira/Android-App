package site.pnpl.mira.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import site.pnpl.mira.data.database.check_in.entity.CheckIn
import site.pnpl.mira.ui.home.recycler_view.CheckInAdapter.Companion.TYPE_ITEM_CHECK_IN

@Parcelize
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
    var isSelected: Boolean,
    val typeItem: Int = TYPE_ITEM_CHECK_IN
) : Parcelable

fun CheckInUI.asCheckIn(): CheckIn {
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