package site.pnpl.mira.ui.greeting.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.models.doOnError
import site.pnpl.mira.data.models.doOnSuccess
import site.pnpl.mira.domain.SettingsProvider
import site.pnpl.mira.databinding.FragmentGreetingBinding
import site.pnpl.mira.models.ExerciseUI
import site.pnpl.mira.ui.extensions.screenHeight
import site.pnpl.mira.ui.greeting.GreetingViewModel
import site.pnpl.mira.utils.ANIMATION_DURATION
import javax.inject.Inject

class GreetingFragment : Fragment(R.layout.fragment_greeting) {

    private var _binding: FragmentGreetingBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var settingsProvider: SettingsProvider

    private val viewModel: GreetingViewModel by viewModels()
    private var yPositionLoading: Float = 0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)
        _binding = FragmentGreetingBinding.bind(view)

        binding.popUpLoading.doOnLayout {
            yPositionLoading = it.y
        }

        setTextName()
        setClickListener()
        setAnimation()
        flowListener()
    }

    private fun flowListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.resultFlow.collect { apiResult ->
                apiResult.doOnSuccess { exercise ->
                    onSuccess(exercise)
                }
                apiResult.doOnError {
                    onError(it)
                }
            }
        }
    }

    private fun onSuccess(exercise: Any) {
        exercise as ExerciseUI
        val extras = bundleOf(
            Pair(
                SCREENS_KEY,
                exercise.screens
            )
        )
        findNavController()
            .navigate(
                R.id.action_to_acquaintance_fragment,
                extras
            )
    }

    private fun onError(errorText: String?) {
        with(binding.popUpLoading) {
            enableProgressBar(false)
            setBody(getString(R.string.pop_up_loading_body_1))
            setTextButton(getString(R.string.pop_up_loading_button_1))
            setButtonClickListener {
                binding.blackout.isVisible = false
                animate()
                    .y(screenHeight.toFloat())
                    .alpha(0f)
                    .setDuration(ANIMATION_DURATION)
                    .start()
            }
        }
        Toast.makeText(requireContext(), errorText, Toast.LENGTH_SHORT).show()
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
            startLoading()
        }

        binding.skip.setOnClickListener {
            settingsProvider.firstLaunchCompleted()
            findNavController().navigate(R.id.action_greeting_to_home)
        }
    }

    private fun startLoading() {
        with(binding.popUpLoading) {

            y = screenHeight.toFloat()
            enableProgressBar(true)
            animate()
                .alpha(1.0f)
                .y(yPositionLoading)
                .setDuration(ANIMATION_DURATION)
                .start()
        }
        binding.blackout.isVisible = true
        viewModel.getIntroExercise()
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
    
    companion object {
        const val SCREENS_KEY = "screens"
    }
}