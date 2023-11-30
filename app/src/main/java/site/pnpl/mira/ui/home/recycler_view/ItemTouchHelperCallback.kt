package site.pnpl.mira.ui.home.recycler_view

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchHelperCallback(
    private val onChangeExpandedListener: ChangeExpandedListener
) : ItemTouchHelper.Callback() {

    var isExpanded: Boolean = false

    override fun isLongPressDragEnabled(): Boolean = false

    override fun isItemViewSwipeEnabled(): Boolean = true
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        println("onSwiped: isExpanded $isExpanded")
        if (isExpanded) {
            onChangeExpandedListener.expandAll(false)
        } else {
            onChangeExpandedListener.expandAll(true)

        }
    }

    override fun getMoveThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return MOVE_THRESHOLD
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (dX < viewHolder.itemView.width * MOVE_THRESHOLD) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    companion object {
        const val MOVE_THRESHOLD = 0.4f
    }
}