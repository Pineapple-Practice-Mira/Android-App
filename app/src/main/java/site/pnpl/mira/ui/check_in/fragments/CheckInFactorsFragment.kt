package site.pnpl.mira.ui.check_in.fragments

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentCheckInFactorsBinding
import site.pnpl.mira.entity.FactorsList
import site.pnpl.mira.ui.check_in.custoview.FactorView
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
//        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        fillFactors()

        binding.noteInput.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.noteInput.setRawInputType(InputType.TYPE_CLASS_TEXT)
        setListener()
    }

    private fun fillFactors() {
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
                println("binding.btnNext.isEnabled ${binding.btnDone.isEnabled}")
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

//        activity?.let {
//            KeyboardVisibilityEvent.setEventListener(it) { isOpen ->
//                factorsButtons.forEach { factorView ->
//                    val animation = AnimationUtils.loadAnimation(
//                        requireContext(), if (isOpen) R.anim.hide_buttons else R.anim.show_buttons).apply {
//                        setAnimationListener(object : AnimationListener{
//                            override fun onAnimationStart(p0: Animation?) {}
//
//                            override fun onAnimationEnd(p0: Animation?) {
//                                if (isOpen) factorView.alpha = 0f else factorView.alpha = 1f
//                            }
//                            override fun onAnimationRepeat(p0: Animation?) {}
//                        })
//                    }
//                    factorView.startAnimation(animation)
//                }
//            }
//        }

//        KeyboardUtils.addKeyboardToggleListener(requireActivity(), object : SoftKeyboardToggleListener {
//            override fun onToggleSoftKeyboard(isVisible: Boolean) {
//                factorsButtons.forEach { factorView ->
//                    val animation = AnimationUtils.loadAnimation(
//                        requireContext(), if (isVisible) R.anim.hide_buttons else R.anim.show_buttons).apply {
//                        setAnimationListener(object : AnimationListener{
//                            override fun onAnimationStart(p0: Animation?) {}
//
//                            override fun onAnimationEnd(p0: Animation?) {
//                                if (isVisible) factorView.alpha = 0f else factorView.alpha = 1f
//                            }
//                            override fun onAnimationRepeat(p0: Animation?) {}
//                        })
//                    }
//                    factorView.startAnimation(animation)
//                }
//            }
//        })
//            noteInput.setOnEditorActionListener { p0, id, p2 ->
//                if (id == EditorInfo.IME_ACTION_DONE) {
//                    hideAndShowViews(false)
//                }
//                false
//            }
//            noteInput.setOnClickListener {
//                if (!isHidden) {
//                    hideAndShowViews(true)
//                }
//            }
        }
    }

    private fun hideAndShowViews(isHide: Boolean) {
        factorsButtons.forEach { factorView ->
            val animation = AnimationUtils.loadAnimation(
                requireContext(), if (isHide) R.anim.hide_buttons else R.anim.show_buttons
            ).apply {
                setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {}

                    override fun onAnimationEnd(p0: Animation?) {
                        if (isHide) factorView.alpha = 0f else factorView.alpha = 1f
                    }

                    override fun onAnimationRepeat(p0: Animation?) {}
                })
            }
            factorView.startAnimation(animation)
        }
        isHidden = isHide
    }

    override fun onDestroyView() {
        super.onDestroyView()
        KeyboardUtils.removeAllKeyboardToggleListeners()
        _binding = null
    }
}