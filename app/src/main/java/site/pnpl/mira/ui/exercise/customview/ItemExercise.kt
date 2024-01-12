package site.pnpl.mira.ui.exercise.customview

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import site.pnpl.mira.R
import site.pnpl.mira.databinding.ItemExerciseBinding

class ItemExercise @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attributeSet, defStyleAttr) {

    private var _binding: ItemExerciseBinding? = null
    private  val binding get() = _binding!!

    init {
        _binding = ItemExerciseBinding.bind(inflate(context, R.layout.item_exercise, this))
//        _binding = ItemExerciseBinding.inflate(LayoutInflater.from(context), this, false)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}