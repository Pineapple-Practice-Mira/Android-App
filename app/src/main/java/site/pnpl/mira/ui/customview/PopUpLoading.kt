package site.pnpl.mira.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import site.pnpl.mira.R
import site.pnpl.mira.databinding.PopUpLoadingBinding

class PopUpLoading(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    private var _binding: PopUpLoadingBinding? = null
    private val binding: PopUpLoadingBinding get() = _binding!!

    init {
        _binding = PopUpLoadingBinding.bind(LayoutInflater.from(context).inflate(R.layout.pop_up_loading, this))
        setAnimation()
    }

    fun enableProgressBar(value: Boolean) {
        if (value) {
            binding.body.isVisible = false
            binding.button.isVisible = false
        }
        binding.progressBar.isVisible = value
    }

    private fun setAnimation() {
        Glide.with(context)
            .asGif()
            .load(R.drawable.progress_bar)
            .into(binding.progressBar)
    }

    fun setBody(text:String) {
        binding.body.apply {
            this.text = text
            isVisible = true
        }
    }

    fun setTextButton(text: String) {
        binding.button.apply {
            this.text = text
            isVisible = true
        }
    }


    fun setButtonClickListener(listener: () -> Unit) {
        binding.button.setOnClickListener {
            listener()
        }
    }
}