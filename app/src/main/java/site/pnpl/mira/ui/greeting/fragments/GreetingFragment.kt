package site.pnpl.mira.ui.greeting.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.databinding.FragmentGreetingBinding
import javax.inject.Inject

class GreetingFragment : Fragment(R.layout.fragment_greeting) {

    private var _binding: FragmentGreetingBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var settingsProvider: SettingsProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)
        _binding = FragmentGreetingBinding.bind(view)

        setTextName()
        setClickListener()
        setAnimation()
    }

    private fun setTextName() {
        if (settingsProvider.getName() != "") {
            var titleText = binding.textViewName.text.toString()
            titleText = "$titleText,\n${settingsProvider.getName()}"
            binding.textViewName.text = titleText
        }
    }

    private fun setClickListener() {
        binding.greeting.setOnClickListener {
            findNavController().navigate(R.id.action_to_acquaintance_fragment)
        }

        binding.skip.setOnClickListener {
            settingsProvider.firstLaunchCompleted()
            findNavController().navigate(R.id.action_greeting_to_home)
        }
    }

    private fun setAnimation() {
        Glide.with(requireContext())
            .asGif()
            .load(R.drawable.gif_greeting)
            .into(binding.animation)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}