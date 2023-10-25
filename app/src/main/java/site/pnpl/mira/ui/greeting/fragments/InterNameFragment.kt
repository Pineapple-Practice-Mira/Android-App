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
        val sunAnim = ObjectAnimator.ofFloat(sun, View.TRANSLATION_X, 500f, 0f)
        sunAnim.duration = 1000
        val nightAnim = ObjectAnimator.ofFloat(interName, View.ALPHA, 0.5f, 1f)
        nightAnim.duration = 500
        val mountAnim1 = ObjectAnimator.ofFloat(mountain1, View.TRANSLATION_Y, 500f, 0f)
        mountAnim1.duration = 1000
        val mountAnim2 = ObjectAnimator.ofFloat(mountain2, View.TRANSLATION_Y, 800f, 0f)
        mountAnim2.duration = 1000
        val mountAnim3 = ObjectAnimator.ofFloat(mountain3, View.TRANSLATION_Y, 2000f, 0f)
        mountAnim3.duration = 1000

        val animatorSun = AnimatorSet()
        animatorSun.playTogether(sunAnim, nightAnim,mountAnim3,mountAnim2,mountAnim1 )
        animatorSun.start()

    }
}