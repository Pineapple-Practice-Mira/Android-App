package site.pnpl.mira.ui.greeting.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.databinding.FragmentGreetingBinding
import javax.inject.Inject

class GreetingFragment : Fragment() {
    private var _binding: FragmentGreetingBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var settingsProvider: SettingsProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGreetingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}