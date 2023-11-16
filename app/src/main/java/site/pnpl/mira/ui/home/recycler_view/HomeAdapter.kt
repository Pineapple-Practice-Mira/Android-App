package site.pnpl.mira.ui.home.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import site.pnpl.mira.R
import site.pnpl.mira.data.entity.CheckIn
import site.pnpl.mira.databinding.ItemCheckInBinding
import site.pnpl.mira.entity.Emotion
import site.pnpl.mira.entity.EmotionsList
import site.pnpl.mira.utils.MiraDateFormat

class HomeAdapter() : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private val checkIns = mutableListOf<CheckIn>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_check_in, parent, false)
        return ViewHolder(ItemCheckInBinding.bind(view))
    }

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
                        Emotion.Type.POSITIVE -> AppCompatResources.getColorStateList(context, R.color.emotion_view_text_color_positive_state)
                        Emotion.Type.NEGATIVE -> AppCompatResources.getColorStateList(context, R.color.emotion_view_text_color_negative_state)
                    }
                )
            }
            holder.emotionDrawable.setImageDrawable(
                AppCompatResources.getDrawable(
                    holder.itemView.context,
                    EmotionsList.emotions[emotionId].emojiResId
                )
            )
        }


    }

    fun setItemsList(items: List<CheckIn>) {
        checkIns.clear()
        checkIns.addAll(items)
        update()
    }

    fun addItem(item: CheckIn) {
        checkIns.add(item)
        update()
    }

    private fun update() {
        notifyDataSetChanged()
//        notifyItemInserted(items.size - 1)
    }

    class ViewHolder(binding: ItemCheckInBinding) : RecyclerView.ViewHolder(binding.root) {
        val context = binding.root.context
        val day = binding.day
        val month = binding.month
        val dayOfWeekAndTime = binding.dayOfWeekAndTime
        val emotion = binding.emotion
        val emotionDrawable = binding.emotionDrawable
    }

}



