package site.pnpl.mira.ui.exercise.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentExerciseElementBinding
import site.pnpl.mira.models.ScreenUI
import site.pnpl.mira.ui.extensions.getParcelableCompat
import site.pnpl.mira.utils.GlideListener

class ElementExerciseFragment() : Fragment(R.layout.fragment_exercise_element) {

    private var _binding: FragmentExerciseElementBinding? = null
    private val binding: FragmentExerciseElementBinding get() = _binding!!
    private var screenUI: ScreenUI? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExerciseElementBinding.bind(view)

        screenUI = arguments?.getParcelableCompat(KEY_SCREENS)

        showProgressBar(true)
        setContent()
    }

    private fun setContent() {
        binding.apply {
            text.text = screenUI?.text
            title.text = screenUI?.title

            Glide.with(requireContext())
                .asGif()
                .load(screenUI?.animationLink)
//                .thumbnail(Glide.with(requireContext()).asGif().load(R.drawable.progress_bar).fitCenter())
                .listener(GlideListener.OnCompletedGif {
                    showProgressBar(false)
                })
                .into(animation)
        }
    }

    private fun showProgressBar(value: Boolean) {
        binding.progressBar.isVisible = value
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_SCREENS = "screens"
    }
}

