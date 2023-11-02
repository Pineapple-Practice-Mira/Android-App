package site.pnpl.mira.ui.greeting.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.databinding.FragmentSplashBinding
import site.pnpl.mira.ui.greeting.GreetingActivity
import site.pnpl.mira.ui.main.MainActivity
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

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            if (settingsProvider.isFirstLaunch()) {
                (activity as GreetingActivity).navController.navigate(R.id.action_splashFragment_to_interNameFragment)
            } else {
                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        Intent(requireActivity(), MainActivity::class.java).apply {
            startActivity(this)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}