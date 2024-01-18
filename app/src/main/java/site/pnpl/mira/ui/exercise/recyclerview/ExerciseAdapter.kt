package site.pnpl.mira.ui.exercise.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import site.pnpl.mira.models.ExerciseUI
import site.pnpl.mira.ui.exercise.customview.ItemExercise

class ExerciseAdapter(
    private val listener: ItemClickListener
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {
    private val exercises: MutableList<ExerciseUI> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemExercise = ItemExercise(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return ViewHolder(itemExercise)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exerciseUI = exercises[position]
        holder.init(exerciseUI)
        holder.itemExercise.setOnClickListener {
            listener.onClick(exerciseUI)
        }
    }

    class ViewHolder(val itemExercise: View) : RecyclerView.ViewHolder(itemExercise) {

        fun init(exerciseUI: ExerciseUI) {
            itemExercise as ItemExercise
            itemExercise.setImage(exerciseUI.previewImageLink)
            itemExercise.setState(ItemExercise.State.NORMAL, exerciseUI.name)
        }
    }

    fun submitList(newList: List<ExerciseUI>) {
//        val diff = ExerciseDiff(exercises, newList)
//        val diffResult = DiffUtil.calculateDiff(diff)

        exercises.clear()
        exercises.addAll(newList)
        notifyDataSetChanged()
//        diffResult.dispatchUpdatesTo(this)
    }

    interface ItemClickListener {
        fun onClick(exerciseUI: ExerciseUI)
    }
    class ExerciseDiff(
        private val oldList: List<ExerciseUI>,
        private val newList: List<ExerciseUI>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

    }
}