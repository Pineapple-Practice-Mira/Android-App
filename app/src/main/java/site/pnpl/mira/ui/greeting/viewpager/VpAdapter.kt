package site.pnpl.mira.ui.greeting.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class VpAdapter(fragment: FragmentActivity, private val vpElements: List<VpElement>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return vpElements.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = ElementFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(ARG_OBJECT, vpElements[position])
        }
        return fragment
    }
}