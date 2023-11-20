package site.pnpl.mira.ui.home.recycler_view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import site.pnpl.mira.R
import site.pnpl.mira.databinding.ItemCheckInExpandedBinding
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.model.Emotion
import site.pnpl.mira.model.EmotionsList
import site.pnpl.mira.ui.home.HomeFragment
import site.pnpl.mira.utils.MiraDateFormat

class CheckInAdapter(
    private val isExpanded: Boolean,
    private val changeExpandedListener: HomeFragment.ChangeExpandedListener,
    private val onSelectedItemsListener: HomeFragment.SelectedItemsListener
) : RecyclerView.Adapter<CheckInAdapter.ViewHolder>() {
    val checkIns = mutableListOf<CheckInUI>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (isExpanded) {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_check_in_expanded, parent, false))
        } else {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_check_in, parent, false))
        }
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

            holder.itemView.setOnLongClickListener {
                changeExpandedListener.expandAll(!isExpanded)
                true
            }

            holder.itemView.setOnClickListener {
                if (isExpanded) {
                    holder.selected()
                }
            }
            if (isExpanded) {
                holder.itemView.findViewById<ImageButton>(R.id.selector).isSelected = isSelected
                holder.itemView.findViewById<ConstraintLayout>(R.id.main).isSelected = isSelected
            }

        }
    }

    fun selectAll(value: Boolean) {
        checkIns.forEach { checkIn ->
            checkIn.isSelected = value
        }
        update()
        notifyIfHaveSelectedItems()
    }

    fun notifyIfHaveSelectedItems() {
        var isHaveSelected = false
        checkIns.forEach { checkIn ->
            if (checkIn.isSelected) {
                isHaveSelected = true
                return@forEach
            }
        }
        onSelectedItemsListener.notify(isHaveSelected)
    }

    fun setItemsList(items: List<CheckInUI>) {
        checkIns.clear()
        checkIns.addAll(items)
        if (!isExpanded) {
            checkIns.forEach { it.isSelected = false }
        }
        update()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun update() {
        notifyDataSetChanged()
//        notifyItemInserted(items.size - 1)
    }

    fun getSelectedItemsAndDelete(): List<CheckInUI> {
        val list = mutableListOf<CheckInUI>()
        checkIns.forEach { checkIn ->
            if (checkIn.isSelected) {
                list.add(checkIn)
            }
        }
        removeSelectedItems(list)
        return list
    }

    private fun removeSelectedItems(list: List<CheckInUI>) {
        checkIns.removeAll(list)
        selectAll(false)
    }

    inner class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        val context: Context = item.context
        val day: TextView = item.findViewById(R.id.day)
        val month: TextView = item.findViewById(R.id.month)
        val dayOfWeekAndTime: TextView = item.findViewById(R.id.dayOfWeekAndTime)
        val emotion: TextView = item.findViewById(R.id.emotion)
        val emotionDrawable: ImageView = item.findViewById(R.id.emotionDrawable)

        fun selected(value: Boolean? = null) {
            if (!isExpanded) return
            ItemCheckInExpandedBinding.bind(item).apply {
                selector.isSelected = value ?: !selector.isSelected
                main.isSelected = value ?: !main.isSelected
                checkIns[adapterPosition].isSelected = value ?: !checkIns[adapterPosition].isSelected
                notifyIfHaveSelectedItems()
            }
        }
    }
}



