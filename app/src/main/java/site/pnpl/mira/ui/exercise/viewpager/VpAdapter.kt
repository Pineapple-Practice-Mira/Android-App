package site.pnpl.mira.ui.exercise.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import site.pnpl.mira.models.ScreenUI
import java.util.ArrayList

class VpAdapter(fragment: FragmentActivity, private val screens: ArrayList<ScreenUI>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = screens.size

    override fun createFragment(position: Int): Fragment = ElementExerciseFragment(screens[position])
}