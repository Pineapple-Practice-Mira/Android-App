package site.pnpl.mira.ui.exercise.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentExerciseElementBinding
import site.pnpl.mira.models.ScreenUI
import site.pnpl.mira.ui.extensions.getParcelableCompat

class ElementExerciseFragment() : Fragment(R.layout.fragment_exercise_element) {

    private var _binding: FragmentExerciseElementBinding? = null
    private val binding: FragmentExerciseElementBinding get() = _binding!!
    private var screenUI: ScreenUI? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExerciseElementBinding.bind(view)

        screenUI = arguments?.getParcelableCompat(KEY_SCREENS)

        setContent()
    }

    private fun setContent() {
        showProgressBar(true)
        binding.apply {
            text.text = screenUI?.text
            title.text = screenUI?.title

            Glide.with(requireContext())
                .asGif()
                .load(screenUI?.animationLink)
                .listener(gifLoaderListener)
                .into(animation)
        }
    }

    private val gifLoaderListener = object : RequestListener<com.bumptech.glide.load.resource.gif.GifDrawable>{
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<com.bumptech.glide.load.resource.gif.GifDrawable>,
            isFirstResource: Boolean
        ): Boolean {
            showProgressBar(false)
            return false
        }

        override fun onResourceReady(
            resource: com.bumptech.glide.load.resource.gif.GifDrawable,
            model: Any,
            target: Target<com.bumptech.glide.load.resource.gif.GifDrawable>?,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            showProgressBar(false)
            return false
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

