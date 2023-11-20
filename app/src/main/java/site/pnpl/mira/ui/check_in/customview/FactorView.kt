package site.pnpl.mira.ui.check_in.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FactorViewBinding
import site.pnpl.mira.entity.Factor

class FactorView constructor(context: Context) : LinearLayout(context) {

    constructor(context: Context, attributeSet: AttributeSet) : this(context)

    private var _binding: FactorViewBinding? = null
    private val binding: FactorViewBinding get() = _binding!!
    private var factor: Factor? = null
    var factorId = -1
        get() = factor?.id ?: -1
        private set

    init {
        _binding = FactorViewBinding.bind(LayoutInflater.from(context).inflate(R.layout.factor_view, this))
    }

    fun setData(factor: Factor) {
        this.factor = factor
        binding.factorName.text = resources.getString(factor.nameResId)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}