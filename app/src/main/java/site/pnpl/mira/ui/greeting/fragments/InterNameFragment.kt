package site.pnpl.mira.ui.greeting.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.databinding.FragmentInterNameBinding
import site.pnpl.mira.utils.ANIMATION_TIME_INPUT_NAME
import site.pnpl.mira.utils.ANIMATION_TIME_INPUT_NAME_ALPHA
import site.pnpl.mira.utils.InputLettersFilter
import site.pnpl.mira.utils.MIN_LENGTH_IN_INPUT_NAME
import javax.inject.Inject

class InterNameFragment : Fragment() {
    private var _binding: FragmentInterNameBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var settingsProvider: SettingsProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInterNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

        // Анимация Солнца и гор
        startAnimBackground()

        var name = settingsProvider.getName()
        with(binding) {

            confirm.isEnabled = name.length >= MIN_LENGTH_IN_INPUT_NAME

            inputName.apply {
                text = SpannableStringBuilder(name)
                filters = arrayOf(InputLettersFilter())
            }

            inputName.doAfterTextChanged { editable ->
                name = editable.toString()
                confirm.isEnabled = name.length >= MIN_LENGTH_IN_INPUT_NAME
            }

            confirm.setOnClickListener {
                settingsProvider.saveName(name)
                navigateToNextFragment()
            }

            skip.setOnClickListener {
                navigateToNextFragment()
            }
        }
    }

    private fun navigateToNextFragment() {
        findNavController().navigate(R.id.action_inter_name_to_greeting_fragment)
    }


    private fun startAnimBackground() = with(binding) {
        val sunAnim = ObjectAnimator.ofFloat(sun, View.TRANSLATION_X, 500f, 0f)
        sunAnim.duration = ANIMATION_TIME_INPUT_NAME
        val nightAnim = ObjectAnimator.ofFloat(binding.root, View.ALPHA, 0.5f, 1f)
        nightAnim.duration = ANIMATION_TIME_INPUT_NAME_ALPHA
        val mountAnim1 = ObjectAnimator.ofFloat(mountain1, View.TRANSLATION_Y, 500f, 0f)
        mountAnim1.duration = ANIMATION_TIME_INPUT_NAME
        val mountAnim2 = ObjectAnimator.ofFloat(mountain2, View.TRANSLATION_Y, 800f, 0f)
        mountAnim2.duration = ANIMATION_TIME_INPUT_NAME
        val mountAnim3 = ObjectAnimator.ofFloat(mountain3, View.TRANSLATION_Y, 2000f, 0f)
        mountAnim3.duration = ANIMATION_TIME_INPUT_NAME

        val animatorSun = AnimatorSet()
        animatorSun.playTogether(sunAnim, nightAnim, mountAnim3, mountAnim2, mountAnim1)
        animatorSun.start()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}