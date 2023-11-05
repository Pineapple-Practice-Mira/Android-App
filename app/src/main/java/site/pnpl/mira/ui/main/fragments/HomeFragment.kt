package site.pnpl.mira.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentHomeBinding
import site.pnpl.mira.ui.customview.BottomBar
import site.pnpl.mira.ui.customview.BottomBar.Companion.HOME
import site.pnpl.mira.ui.main.fragments.CheckInCompletedFragment.Companion.CALLBACK_HOME
import site.pnpl.mira.ui.main.fragments.CheckInCompletedFragment.Companion.CALLBACK_KEY

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stubClickListener()

        initBottomBar()
        setClickListener()

    }

    private fun initBottomBar() {

        binding.bottomBar.setFocusedButton(HOME)
        binding.bottomBar.setClickListener(object : BottomBar.BottomBarClicked {
            override fun onClick(button: BottomBar.BottomBarButton) {
                when (button) {
                    BottomBar.BottomBarButton.HOME -> {}
                    BottomBar.BottomBarButton.EXERCISES_LIST -> {
                        findNavController().navigate(R.id.action_home_to_exercises)
                    }
                    BottomBar.BottomBarButton.CHECK_IN -> {
                        findNavController().navigate(R.id.createCheckIn, bundleOf(Pair(CALLBACK_KEY, CALLBACK_HOME)))
                    }
                }
            }
        })
    }

    private fun setClickListener() {

    }

    private fun stubClickListener() {
        with(binding) {

            settings.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_setting)
            }
            statistic.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_statistics)
            }

            openCheckIn.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_details)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}