package site.pnpl.mira.ui.check_in.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentCheckInFeelingBinding
import site.pnpl.mira.entity.Emotion
import site.pnpl.mira.entity.EmotionsList
import site.pnpl.mira.ui.check_in.custoview.EmotionView

class CheckInFeelFragment(private val listener: CheckInFragment.OnArrowClickListener) : Fragment() {
    private var _binding: FragmentCheckInFeelingBinding? = null
    private val binding get() = _binding!!
    private val emotionsButtons = mutableListOf<EmotionView>()
    private var emotionId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckInFeelingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.isEnabled = false
        @Suppress("DEPRECATION")
        requireActivity().window.statusBarColor = resources.getColor(R.color.dark_grey)
        fillEmotions()

//        val key = findNavController().currentBackStackEntry?.arguments?.getString(CALLBACK_KEY)
//        binding.next.setOnClickListener {
//            findNavController().navigate(R.id.action_feel_to_factors, bundleOf(Pair(CALLBACK_KEY, key)))
//        }
//        binding.close.setOnClickListener {
//            findNavController().popBackStack()
//        }

        binding.root.setOnClickListener {
//            findNavController().navigate(R.id.action_feel_to_factors)
        }
        setClickListener()
    }

    private fun fillEmotions() {
        EmotionsList.emotions.forEach { emotion: Emotion ->
            val emotionView = EmotionView(requireContext()).apply {
                setData(emotion)
            }
            when(emotion.type) {
                Emotion.Type.POSITIVE -> {
                    binding.positiveContainer.addView(emotionView)
                }
                Emotion.Type.NEGATIVE -> {
                    binding.negativeContainer.addView(emotionView)
                }
            }
            emotionsButtons.add(emotionView)
        }
        setOnClickEmotions()
    }

    private fun setOnClickEmotions() {
        emotionsButtons.forEach { emotionView ->
            emotionView.findViewById<LinearLayout>(R.id.container).setOnClickListener {
                binding.btnNext.isEnabled = false
                emotionsButtons.forEach {
                    if (it != emotionView) {
                        it.isSelected = false
                    }
                }
                emotionView.isSelected= !emotionView.isSelected
                emotionsButtons.forEach {
                    if (it.isSelected) {
                        binding.btnNext.isEnabled = true
                        emotionId = it.emotionId
                    }
                }
                println("binding.btnNext.isEnabled ${binding.btnNext.isEnabled}")
            }
        }
    }

    private fun setClickListener() {
        with(binding) {
            btnNext.setOnClickListener {
                listener.onClick(true, emotionId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}