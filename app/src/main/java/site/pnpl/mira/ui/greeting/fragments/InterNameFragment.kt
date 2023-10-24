package site.pnpl.mira.ui.greeting.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import site.pnpl.mira.App
import site.pnpl.mira.ui.greeting.GreetingActivity
import site.pnpl.mira.R
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.databinding.FragmentInterNameBinding
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

        // Анимация Солнца и освещения гор
        sunAnim()


        //Врмееменая заглушка, для перехода между активити
        binding.next.setOnClickListener {
            (activity as GreetingActivity).navController.navigate(R.id.action_interNameFragment_to_greetingFragment)
        }

        // Введеное имя отправить и сохранить так:
        //settingsProvider.saveName(name = name)
        // После сохранения для перехода дальше вызвать:
        // (activity as GreetingActivity).navController.navigate(R.id.action_interNameFragment_to_greetingFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun sunAnim() = with(binding) {
        val sunAnim = ObjectAnimator.ofFloat(sun, View.TRANSLATION_X, -200f)
        sunAnim.duration = 500

        val nightAnim = ObjectAnimator.ofFloat(mountains, View.ALPHA, 0f)
        nightAnim.duration = 1500

        val animatorSun = AnimatorSet()
        animatorSun.playTogether(sunAnim, nightAnim)
        animatorSun.start()
    }
}