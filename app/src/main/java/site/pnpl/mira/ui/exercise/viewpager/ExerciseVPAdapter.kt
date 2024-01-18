package site.pnpl.mira.ui.exercise.viewpager

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import site.pnpl.mira.models.ScreenUI
import site.pnpl.mira.ui.exercise.viewpager.ElementExerciseFragment.Companion.KEY_SCREENS
import java.util.ArrayList

class ExerciseVPAdapter(fragment: FragmentActivity, private val screens: ArrayList<ScreenUI>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = screens.size

    override fun createFragment(position: Int): Fragment =
        ElementExerciseFragment().apply {
            arguments = bundleOf(Pair(KEY_SCREENS, screens[position]))
        }
}