package site.pnpl.mira.ui.greeting.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.SettingsProvider
import javax.inject.Inject

class SplashFragment : Fragment(R.layout.fragment_splash) {

    @Inject lateinit var settingsProvider: SettingsProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            if (settingsProvider.isFirstLaunch()) {
                findNavController().navigate(R.id.action_splashFragment2_to_greeting_graph)
            } else {
                findNavController().navigate(R.id.action_splash_to_navigation_home)
            }
        }
    }
}