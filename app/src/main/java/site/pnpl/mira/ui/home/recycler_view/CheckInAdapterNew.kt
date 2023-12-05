package site.pnpl.mira.ui.home.recycler_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import site.pnpl.mira.R
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.model.Emotion
import site.pnpl.mira.model.EmotionsList
import site.pnpl.mira.utils.MiraDateFormat

class CheckInAdapterNew : PagingDataAdapter<CheckInUI, CheckInAdapterNew.CheckInViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckInViewHolder {
        return CheckInViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_check_in, parent, false))
    }
    override fun onBindViewHolder(holder: CheckInViewHolder, position: Int) {
        getItem(position)?.let { checkInUI ->
            holder.bind(checkInUI)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CheckInUI>() {
            override fun areItemsTheSame(oldItem: CheckInUI, newItem: CheckInUI): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CheckInUI, newItem: CheckInUI): Boolean =
                oldItem == newItem
        }
    }

    class CheckInViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val context: Context = view.context
        val day: TextView = view.findViewById(R.id.day)
        val month: TextView = view.findViewById(R.id.month)
        val dayOfWeekAndTime: TextView = view.findViewById(R.id.dayOfWeekAndTime)
        val emotion: TextView = view.findViewById(R.id.emotion)
        val emotionDrawable: ImageView = view.findViewById(R.id.emotionDrawable)
        fun bind(item: CheckInUI) {
            item.apply {
                val date = MiraDateFormat(createdAtLong)
                day.text = date.getDateOfMonth()
                month.text = date.getNameMonth()
                dayOfWeekAndTime.text = date.getDayOfWeekAndTime()

                emotion.apply {
                    val emotionText = "#${context.resources.getString(EmotionsList.emotions[emotionId].nameResId)}"
                    text = emotionText
                    setTextColor(
                        when (EmotionsList.emotions[emotionId].type) {
                            Emotion.Type.POSITIVE -> AppCompatResources.getColorStateList(context, R.color.primary)
                            Emotion.Type.NEGATIVE -> AppCompatResources.getColorStateList(context, R.color.third)
                        }
                    )
                }
                emotionDrawable.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        EmotionsList.emotions[emotionId].emojiResId
                    )
                )

            }
        }

    }
}
