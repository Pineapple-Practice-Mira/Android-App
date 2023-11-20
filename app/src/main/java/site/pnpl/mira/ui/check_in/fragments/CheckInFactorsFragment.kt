package site.pnpl.mira.ui.check_in.fragments

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentCheckInFactorsBinding
import site.pnpl.mira.entity.FactorsList
import site.pnpl.mira.ui.check_in.customview.FactorView
import site.pnpl.mira.utils.KeyboardUtils


class CheckInFactorsFragment(
    private val onArrowClickListener: CheckInFragment.OnArrowClickListener,
    private val onSaveClickListener: CheckInFragment.OnSaveClickListener
) : Fragment(R.layout.fragment_check_in_factors) {
    private var _binding: FragmentCheckInFactorsBinding? = null
    private val binding get() = _binding!!
    private val factorsButtons = mutableListOf<FactorView>()
    private var factorId = -1
    private var isHidden = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCheckInFactorsBinding.bind(view)
        binding.btnDone.isEnabled = false
        fillFactors()

        binding.noteInput.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.noteInput.setRawInputType(InputType.TYPE_CLASS_TEXT)
        setListener()

        keyboardListener()
    }

    private fun keyboardListener() {
        ViewCompat.setWindowInsetsAnimationCallback(
            requireView(),
            object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
                override fun onProgress(
                    insets: WindowInsetsCompat,
                    runningAnimations: MutableList<WindowInsetsAnimationCompat>
                ): WindowInsetsCompat = insets

                override fun onStart(
                    animation: WindowInsetsAnimationCompat,
                    bounds: WindowInsetsAnimationCompat.BoundsCompat
                ): WindowInsetsAnimationCompat.BoundsCompat {

                    if (animation.typeMask == WindowInsetsCompat.Type.ime()) {
                        hideAndShowViews(!isHidden)
                    }
                    return super.onStart(animation, bounds)
                }
            }
        )
    }

    private fun fillFactors() {
        binding.leftContainer.removeAllViews()
        binding.rightContainer.removeAllViews()
        FactorsList.factors.forEachIndexed { index, factor ->
            val factorView = FactorView(requireContext()).apply {
                setData(factor)
            }
            if (index < FactorsList.factors.size / 2) {
                binding.leftContainer.addView(factorView)
            } else {
                binding.rightContainer.addView(factorView)
            }
            factorsButtons.add(factorView)
        }
        setOnClickFactors()
    }

    private fun setOnClickFactors() {
        factorsButtons.forEach { factorView ->
            factorView.findViewById<LinearLayout>(R.id.container).setOnClickListener {
                binding.btnDone.isEnabled = false
                factorsButtons.forEach {
                    if (it != factorView) {
                        it.isSelected = false
                    }
                }
                factorView.isSelected = !factorView.isSelected
                factorsButtons.forEach {
                    if (it.isSelected) {
                        binding.btnDone.isEnabled = true
                        factorId = it.factorId
                    }
                }
            }
        }
    }

    private fun setListener() {
        with(binding) {
            btnBack.setOnClickListener {
                onArrowClickListener.onClick(false)
            }
            btnDone.setOnClickListener {
                val note = binding.noteInput.text.toString()
                onSaveClickListener.onClick(factorId = factorId, note = note)
            }
        }
    }

    private fun hideAndShowViews(isHide: Boolean) {
        val visible = if (isHide) View.GONE else View.VISIBLE
        with(binding) {
            bottomBar.visibility = visible
            line.visibility = visible
            btnBack.visibility = visible
            btnDone.visibility = visible
            scrollView.visibility = visible
        }

        isHidden = isHide
    }

    override fun onDestroyView() {
        super.onDestroyView()
        KeyboardUtils.removeAllKeyboardToggleListeners()
        _binding = null
    }
}