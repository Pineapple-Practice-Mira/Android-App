package site.pnpl.mira.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.R

class NavGraphFragment : Fragment(R.layout.fragment_nav_graph) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().navigate(R.id.action_navGraphFragment_to_navigation2)
    }
}