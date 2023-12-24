package site.pnpl.mira.ui.greeting.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.bumptech.glide.Glide
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentElementBinding

class ElementFragment(private val vpElement: VpElement) : Fragment(R.layout.fragment_element) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentElementBinding.bind(view).apply {
            title.text = resources.getString(vpElement.title)
            text.text = resources.getString(vpElement.text)

            Glide.with(requireContext())
                .asGif()
                .load(vpElement.animation)
                .into(animation)
        }
    }
}