package site.pnpl.mira.ui.greeting.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.bumptech.glide.Glide
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentElementBinding
import site.pnpl.mira.models.ScreenUI

class ElementFragment(private val screenUI: ScreenUI) : Fragment(R.layout.fragment_element) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentElementBinding.bind(view).apply {
            text.text = screenUI.text
            title.text = screenUI.title

            Glide.with(requireContext())
                .asGif()
                .load(screenUI.animationLink)
                .into(animation)
        }
    }
}