package site.pnpl.mira.ui.check_in.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentCheckInFactorsBinding
import site.pnpl.mira.entity.FactorsList
import site.pnpl.mira.ui.check_in.custoview.FactorView

class CheckInFactorsFragment(
    private val onArrowClickListener: CheckInFragment.OnArrowClickListener,
    private val onSaveClickListener: CheckInFragment.OnSaveClickListener
) : Fragment() {
    private var _binding: FragmentCheckInFactorsBinding? = null
    private val binding get() = _binding!!
    private val factorsButtons = mutableListOf<FactorView>()
    private var factorId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckInFactorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDone.isEnabled = false
//        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        fillFactors()

//        val key = findNavController().currentBackStackEntry?.arguments?.getString(CALLBACK_KEY)
//        binding.next.setOnClickListener {
//            findNavController().navigate(R.id.action_factors_to_completed, bundleOf(Pair(CALLBACK_KEY, key)))
//        }
//        binding.close.setOnClickListener {
//            findNavController().popBackStack()
//        }

        setClickListener()
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
                factorView.isSelected= !factorView.isSelected
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

    private fun setClickListener() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}