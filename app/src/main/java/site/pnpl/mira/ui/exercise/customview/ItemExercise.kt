package site.pnpl.mira.ui.exercise.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import site.pnpl.mira.R
import site.pnpl.mira.databinding.ItemExerciseBinding

class ItemExercise @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attributeSet, defStyleAttr) {

    private val binding: ItemExerciseBinding
    private var state = State.NORMAL

    init {
        background = null
        binding = ItemExerciseBinding.bind(inflate(context, R.layout.item_exercise, this))
    }

    fun setState(state: State, exerciseName: String = "") {
        this.state = state
        when (state) {
            State.LOADING -> {
                isEnabled = false
                isClickable = false
                binding.mainContainer.isEnabled = false
                binding.progressBar.isVisible = true
                binding.icon.apply {
                    visibility = View.INVISIBLE
                    isClickable = false
                }
                binding.name.apply {
                    text = context.getString(R.string.loading_exercise)
                    setTextColor(ResourcesCompat.getColorStateList(context.resources, R.color.primary, context.theme))
                }
                Glide.with(context)
                    .asGif()
                    .load(R.drawable.progress_bar)
                    .into(binding.progressBar)
            }
            State.ERROR -> {
                isEnabled = false
                isClickable = false
                binding.mainContainer.isEnabled = false
                binding.progressBar.isVisible = false
                binding.icon.apply {
                    visibility = View.VISIBLE
                    setImageDrawable(ResourcesCompat.getDrawable(context. resources, R.drawable.icon_close, context.theme))
                    setColorFilter(ContextCompat.getColor(context, R.color.third))
                    isClickable = false
                }
                binding.name.apply {
                    text = context.getString(R.string.error_loading_exercise)
                    setTextColor(ResourcesCompat.getColorStateList(context.resources, R.color.third, context.theme))
                }
            }
            State.ERROR_WITH_REFRESH -> {
                isEnabled = false
                isClickable = false
                binding.progressBar.isVisible = false
                binding.icon.apply {
                    visibility = View.VISIBLE
                    setImageDrawable(ResourcesCompat.getDrawable(context. resources, R.drawable.icon_refresh, context.theme))
                    setColorFilter(ContextCompat.getColor(context, R.color.primary))
                    isClickable = true
                }
                binding.name.apply {
                    text = context.getString(R.string.error_loading_exercise)
                    setTextColor(ResourcesCompat.getColorStateList(context.resources, R.color.third, context.theme))
                }
            }
            State.NORMAL -> {
                isEnabled = true
                isClickable = true
                binding.progressBar.isVisible = false
                binding.icon.apply {
                    visibility = View.VISIBLE
                    setImageDrawable(ResourcesCompat.getDrawable(context. resources, R.drawable.icon_arrow, context.theme))
                    setColorFilter(ContextCompat.getColor(context, R.color.primary))
                    isClickable = true
                }
                binding.name.apply {
                    text = exerciseName
                    setTextColor(ResourcesCompat.getColorStateList(context.resources, R.color.dark, context.theme))
                }
            }
        }
    }

    fun setClickListener(listener: () -> Unit) {
        binding.icon.setOnClickListener { listener() }
        setOnClickListener {
            listener()
        }
    }

    fun setRefreshClickListener(listener: () -> Unit) {
        binding.icon.setOnClickListener {
            if (state == State.ERROR_WITH_REFRESH) {
                listener()
            }
        }
    }

    fun setImage(imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .into(binding.image)
    }

    enum class State {
        LOADING,
        ERROR,
        ERROR_WITH_REFRESH,
        NORMAL
    }
}