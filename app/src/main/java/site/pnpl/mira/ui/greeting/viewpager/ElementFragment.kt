package site.pnpl.mira.ui.greeting.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentElementBinding
import site.pnpl.mira.models.ScreenUI
import site.pnpl.mira.ui.extensions.getParcelableCompat

class ElementFragment() : Fragment(R.layout.fragment_element) {

    private var _binding: FragmentElementBinding? = null
    private val binding: FragmentElementBinding get() = _binding!!
    private var screenUI: ScreenUI? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentElementBinding.bind(view)

        screenUI = arguments?.getParcelableCompat(KEY_SCREEN)
       binding.apply {
            text.text = screenUI!!.text
            title.text = screenUI!!.title
            showProgressBar(true)
            Glide.with(requireContext())
                .asGif()
                .load(screenUI!!.animationLink)
                .listener(gifLoaderListener)
                .into(animation)
        }
    }

    fun showProgressBar(value: Boolean) {
        binding.progressBar.isVisible = value
    }

    private val gifLoaderListener = object : RequestListener<GifDrawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<GifDrawable>,
            isFirstResource: Boolean
        ): Boolean {
            showProgressBar(false)
            return false
        }

        override fun onResourceReady(
            resource: GifDrawable,
            model: Any,
            target: Target<GifDrawable>?,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            showProgressBar(false)
            return false
        }
    }

    companion object {
        const val KEY_SCREEN = "key_screen"
    }
}

