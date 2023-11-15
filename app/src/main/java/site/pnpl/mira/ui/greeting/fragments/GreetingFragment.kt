package site.pnpl.mira.ui.greeting.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.databinding.FragmentGreetingBinding
import javax.inject.Inject

class GreetingFragment : Fragment(R.layout.fragment_greeting) {

    @Inject lateinit var settingsProvider: SettingsProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

        val binding = FragmentGreetingBinding.bind(view)

        if (settingsProvider.getName() != "") {
            var titleText = binding.textViewName.text.toString()
            titleText = "$titleText,\n${settingsProvider.getName()}"
            binding.textViewName.text = titleText
        }

        binding.greeting.setOnClickListener {
            findNavController().navigate(R.id.action_to_acquaintance_fragment)
        }

        binding.skip.setOnClickListener {
            settingsProvider.firstLaunchCompleted()
            findNavController().navigate(R.id.action_greeting_to_home)
        }
    }
}