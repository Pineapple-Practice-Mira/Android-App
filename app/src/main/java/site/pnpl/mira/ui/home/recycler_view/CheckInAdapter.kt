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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import site.pnpl.mira.R
import site.pnpl.mira.databinding.ItemCheckInExpandedBinding
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.model.Emotion
import site.pnpl.mira.model.EmotionsList
import site.pnpl.mira.utils.MiraDateFormat

class CheckInAdapter(
    private val isExpanded: Boolean,
    private val changeExpandedListener: ChangeExpandedListener,
    private val onSelectedItemsListener: SelectedItemsListener,
    private val onItemClickListener: ItemClickListener
) : RecyclerView.Adapter<CheckInAdapter.ViewHolder>() {
    val checkIns = mutableListOf<CheckInUI>()

    override fun getItemViewType(position: Int): Int {
        return checkIns[position].typeItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == TYPE_ITEM_VOID) {
            VoidViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_check_in_void, parent, false))
        } else {
            if (isExpanded) {
                CheckInViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_check_in_expanded, parent, false))
            } else {
                CheckInViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_check_in, parent, false))
            }
        }
    }

    override fun getItemCount(): Int = checkIns.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (checkIns[position].typeItem == TYPE_ITEM_VOID) return
        holder as CheckInViewHolder
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

            holder.root.setOnLongClickListener {
                changeExpandedListener.expandAll(!isExpanded)
                true
            }

            holder.root.setOnClickListener {
                if (isExpanded) {
                    holder.selected()
                } else {
                    onItemClickListener.onItemClick(position)
                }
            }


            if (isExpanded) {
                val selector: ImageView = holder.itemView.findViewById<ImageButton>(R.id.selector)
                selector.isSelected = isSelected
                holder.itemView.findViewById<ConstraintLayout>(R.id.main).isSelected = isSelected

                selector.setOnLongClickListener {
                    changeExpandedListener.expandAll(!isExpanded)
                    true
                }

                selector.setOnClickListener {
                        holder.selected()
                }
            }

        }
    }

    fun selectAll(value: Boolean) {
        checkIns.forEach { checkIn ->
            if (checkIn.typeItem == TYPE_ITEM_CHECK_IN) {
                checkIn.isSelected = value
            }
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

    fun setItemsList(items: MutableList<CheckInUI>) {
        if (items.isNotEmpty() && items[0].typeItem != TYPE_ITEM_VOID) {
            items.add(0, getVoidCheckIn())
        }

        val diff = CheckInDiff(checkIns, items)
        val diffResult = DiffUtil.calculateDiff(diff)

        checkIns.clear()
        checkIns.addAll(items)

        if (!isExpanded) {
            checkIns.forEach {
                if (it.typeItem == TYPE_ITEM_CHECK_IN) {
                    it.isSelected = false
                }
            }
        }

        diffResult.dispatchUpdatesTo(this)
    }

    private fun getVoidCheckIn(): CheckInUI {
        return CheckInUI(
            id = -1,
            emotionId = -1,
            factorId = -1,
            exercisesId = 0,
            note = "",
            createdAt = "",
            createdAtLong = 0,
            editedAt = "",
            isSynchronized = false,
            isSelected = false,
            typeItem = TYPE_ITEM_VOID
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun update() {
        notifyDataSetChanged()
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

    companion object {
        const val TYPE_ITEM_CHECK_IN = 0
        const val TYPE_ITEM_VOID = 1
    }

    open inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item)

    inner class CheckInViewHolder(val item: View) : ViewHolder(item) {
        val context: Context = item.context
        val root: ConstraintLayout = item.findViewById(R.id.rootContainer)
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

    inner class VoidViewHolder(item: View) : ViewHolder(item)

    class CheckInDiff(
        private val oldList: List<CheckInUI>,
        private val newList: List<CheckInUI>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}



