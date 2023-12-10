package site.pnpl.mira.ui.statistic.recycler_view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import site.pnpl.mira.R
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.model.Emotion
import site.pnpl.mira.model.EmotionsList
import site.pnpl.mira.utils.MiraDateFormat

class CheckInStatisticAdapter(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<CheckInStatisticAdapter.ViewHolder>() {

    val checkIns = mutableListOf<CheckInUI>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_check_in, parent, false))

    override fun getItemCount(): Int = checkIns.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        checkIns[position].apply {
            val date = MiraDateFormat(createdAtLong)
            holder.day.text = date.getDateOfMonth()
            holder.month.text = date.getNameMonth()
            holder.dayOfWeekAndTime.text = date.getDayOfWeekAndTime()

            holder.emotion.apply {
                val emotionText = "#${holder.context.resources.getString(EmotionsList.emotions[emotionId].nameResId)}"
                text = emotionText
                setTextColor(
                    when (EmotionsList.emotions[emotionId].type) {
                        Emotion.Type.POSITIVE -> AppCompatResources.getColorStateList(context, R.color.primary)
                        Emotion.Type.NEGATIVE -> AppCompatResources.getColorStateList(context, R.color.third)
                    }
                )
            }
            holder.emotionDrawable.setImageDrawable(
                AppCompatResources.getDrawable(
                    holder.itemView.context,
                    EmotionsList.emotions[emotionId].emojiResId
                )
            )
            holder.root.setOnClickListener {
                onItemClickListener.onClick(position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemList(items: List<CheckInUI>) {
        if (!checkIns.isEmpty()) checkIns.clear()
        checkIns.addAll(items)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val context: Context = item.context
        val root: ConstraintLayout = item.findViewById(R.id.rootContainer)
        val day: TextView = item.findViewById(R.id.day)
        val month: TextView = item.findViewById(R.id.month)
        val dayOfWeekAndTime: TextView = item.findViewById(R.id.dayOfWeekAndTime)
        val emotion: TextView = item.findViewById(R.id.emotion)
        val emotionDrawable: ImageView = item.findViewById(R.id.emotionDrawable)
    }
}