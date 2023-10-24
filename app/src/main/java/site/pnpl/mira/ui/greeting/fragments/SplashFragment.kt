package site.pnpl.mira.ui.greeting.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import site.pnpl.mira.App
import site.pnpl.mira.ui.greeting.GreetingActivity
import site.pnpl.mira.R
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.databinding.FragmentSplashBinding
import javax.inject.Inject

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var settingsProvider: SettingsProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

        //Временная заглyшка для перехода между фрагментами
        binding.next.setOnClickListener {
            (activity as GreetingActivity).navController.navigate(R.id.action_splashFragment_to_interNameFragment)
        }

        //После анимации лого вызвать - settingsProvider.isFirstLaunch() - если true - первый запуск, вызываем:
        //(activity as GreetingActivity).navController.navigate(R.id.action_splashFragment_to_interNameFragment) для навигации по приветствию
        //если false - значит уже запускалось и старуем MainActivity

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}