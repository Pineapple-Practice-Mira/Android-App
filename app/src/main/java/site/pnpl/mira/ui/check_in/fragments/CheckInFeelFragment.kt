package site.pnpl.mira.ui.check_in.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.LinearLayout
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentCheckInFeelingBinding
import site.pnpl.mira.domain.EmotionProvider
import site.pnpl.mira.models.EmotionUI
import site.pnpl.mira.ui.check_in.customview.EmotionView
import javax.inject.Inject

class CheckInFeelFragment(
    private val onArrowClickListener: CheckInFragment.OnArrowClickListener,
    private val onEmotionClickListener: CheckInFragment.OnEmotionClickListener
) : Fragment(R.layout.fragment_check_in_feeling) {
    private var _binding: FragmentCheckInFeelingBinding? = null
    private val binding get() = _binding!!
    private val emotionsButtons = mutableListOf<EmotionView>()
    private var emotionId = -1

    @Inject
    lateinit var emotionProvider: EmotionProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

        _binding = FragmentCheckInFeelingBinding.bind(view)
        binding.btnNext.isEnabled = false

        fillEmotions()
        setClickListener()
    }

    private fun fillEmotions() {
        emotionProvider.emotions.forEach { emotion: EmotionUI ->
            val emotionView = EmotionView(requireContext()).apply {
                setData(emotion)
            }
            if (emotion.isPositive) {
                binding.positiveContainer.addView(emotionView)
            } else {
                binding.negativeContainer.addView(emotionView)
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
                emotionView.isSelected = !emotionView.isSelected
                emotionsButtons.forEach {
                    if (it.isSelected) {
                        binding.btnNext.isEnabled = true
                        emotionId = it.emotionId

                        onEmotionClickListener.onClick(emotionProvider.getNameGenitive(emotionId))
                    }
                }

                //Если кнопка далее не активна - значит эмоция не выбрана - передаем Null
                if (!binding.btnNext.isEnabled) {
                    onEmotionClickListener.onClick(null)
                }
            }
        }
    }

    private fun setClickListener() {
        with(binding) {
            btnNext.setOnClickListener {
                onArrowClickListener.onClick(true, emotionId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}