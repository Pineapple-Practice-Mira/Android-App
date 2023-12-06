package site.pnpl.mira.ui.check_in.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.ui.check_in.fragments.CheckInDetailsFragment
import site.pnpl.mira.ui.check_in.fragments.CheckInDetailsItemFragment

class DetailAdapter(
    fragment: Fragment,
    private val checkIns: List<CheckInUI>,
    private val onArrowClickListener: CheckInDetailsFragment.ArrowClickListener,
    private val callbackKey: String
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return checkIns.size
    }

    override fun createFragment(position: Int): Fragment {
        return CheckInDetailsItemFragment(checkIns, position, onArrowClickListener, callbackKey)
    }
}