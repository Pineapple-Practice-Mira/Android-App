package site.pnpl.mira.ui.greeting.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class VpAdapter(fragment: FragmentActivity, private val vpElements: List<VpElement>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = vpElements.size

    override fun createFragment(position: Int): Fragment = ElementFragment(vpElements[position])
}