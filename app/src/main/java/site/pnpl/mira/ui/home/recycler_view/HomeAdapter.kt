package site.pnpl.mira.ui.home.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.databinding.ItemCheckInBinding

class HomeAdapter() : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private val items = mutableListOf<CheckInItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_check_in, parent, false)
        return ViewHolder(ItemCheckInBinding.bind(view))
    }

    override fun getItemCount(): Int = App.ITEMS.size
//    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        App.ITEMS[position].apply {
            holder.day.text = day
            holder.month.text = month
            holder.dayOfWeekAndTime.text = dayOfWeekAndTime
            holder.emotion.text = emotion
            holder.emotionDrawable.setImageDrawable(emotionDrawable)
        }
    }

    fun setItemsList(items: List<CheckInItem>) {
        App.ITEMS + items
    }

    fun addItem(item: CheckInItem) {
        App.ITEMS.add(item)
        update()
    }

    fun update() {
        notifyDataSetChanged()
//        notifyItemInserted(items.size - 1)
    }

    class ViewHolder(binding: ItemCheckInBinding) : RecyclerView.ViewHolder(binding.root) {
        val day = binding.day
        val month = binding.month
        val dayOfWeekAndTime = binding.dayOfWeekAndTime
        val emotion = binding.emotion
        val emotionDrawable = binding.emotionDrawable
    }

    object HomeDiffCallback : DiffUtil.ItemCallback<CheckInItem>() {
        override fun areItemsTheSame(oldItem: CheckInItem, newItem: CheckInItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CheckInItem, newItem: CheckInItem): Boolean {
            return oldItem.dateTime == newItem.dateTime

        }
    }

}



