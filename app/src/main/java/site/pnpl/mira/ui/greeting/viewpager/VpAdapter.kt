package site.pnpl.mira.ui.greeting.viewpager

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import site.pnpl.mira.models.ScreenUI
import java.util.ArrayList

class VpAdapter(fragment: Fragment, private val screens: ArrayList<ScreenUI>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = screens.size

    override fun createFragment(position: Int): Fragment =
        ElementFragment().apply {
            arguments = bundleOf(Pair(ElementFragment.KEY_SCREEN, screens[position]))
        }
}