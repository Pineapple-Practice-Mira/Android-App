package site.pnpl.mira.ui.greeting.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentElementBinding
import site.pnpl.mira.models.ScreenUI
import site.pnpl.mira.utils.GlideListener

class ElementFragment(private val screenUI: ScreenUI) : Fragment(R.layout.fragment_element) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentElementBinding.bind(view).apply {
            text.text = screenUI.text
            title.text = screenUI.title
            showProgressBar(true)
            Glide.with(requireContext())
                .asGif()
                .load(screenUI.animationLink)
                .listener(GlideListener.OnCompletedGif{
                    showProgressBar(false)
                })
                .into(animation)
        }
    }
}

fun FragmentElementBinding.showProgressBar(value: Boolean) {
    this.progressBar.isVisible = value
}