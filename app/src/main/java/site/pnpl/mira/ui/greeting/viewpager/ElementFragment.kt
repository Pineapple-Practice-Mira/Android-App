package site.pnpl.mira.ui.greeting.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentElementBinding
import site.pnpl.mira.models.ScreenUI
import site.pnpl.mira.ui.extensions.getParcelableCompat
import site.pnpl.mira.utils.GlideListener

class ElementFragment() : Fragment(R.layout.fragment_element) {

    private var screenUI: ScreenUI? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screenUI = arguments?.getParcelableCompat(KEY_SCREEN)
        FragmentElementBinding.bind(view).apply {
            text.text = screenUI!!.text
            title.text = screenUI!!.title
            showProgressBar(true)
            Glide.with(requireContext())
                .asGif()
                .load(screenUI!!.animationLink)
                .listener(GlideListener.OnCompletedGif{
                    showProgressBar(false)
                })
                .into(animation)
        }
    }

    companion object {
        const val KEY_SCREEN = "key_screen"
    }
}

fun FragmentElementBinding.showProgressBar(value: Boolean) {
    this.progressBar.isVisible = value
}