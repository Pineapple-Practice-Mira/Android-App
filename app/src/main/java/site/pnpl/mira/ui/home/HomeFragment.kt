package site.pnpl.mira.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.core.view.marginBottom
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentHomeBinding
import site.pnpl.mira.ui.check_in.CheckInCompletedFragment.Companion.CALLBACK_HOME
import site.pnpl.mira.ui.check_in.CheckInCompletedFragment.Companion.CALLBACK_KEY
import site.pnpl.mira.ui.customview.BottomBar
import site.pnpl.mira.ui.customview.BottomBar.Companion.HOME
import site.pnpl.mira.ui.home.recycler_view.HomeAdapter
import site.pnpl.mira.ui.home.recycler_view.TopSpacingItemDecoration
import site.pnpl.mira.utils.toPx


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var scope: CoroutineScope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        @Suppress("DEPRECATION")
        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
        stubClickListener()

        initBottomBar()
        initRecyclerView()
        setClickListener()
        setMountainsBottomMargin(DEFAULT_MOUNTAINS_MARGIN.toPx)
        getCheckInData()
    }

    private fun initBottomBar() {

        binding.bottomBar.setSelectedButton(HOME)
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


    private fun initRecyclerView() {
        adapter = HomeAdapter()
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(TopSpacingItemDecoration(12))

    }

    private fun mountainsMarginCorrect() {
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                val mountainsY = binding.mountains.y - EXTRA_MARGIN_MOUNTAINS.toPx
                recyclerView.measure(View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY), View.MeasureSpec.UNSPECIFIED)
                val rvHeight = recyclerView.measuredHeight
                println("rvHeight $rvHeight")
                val rvFullSize = rvHeight + recyclerView.y
                if (rvFullSize > mountainsY) {
                    changeMountainsPos(rvFullSize)
                } else {
                    setMountainsBottomMargin(DEFAULT_MOUNTAINS_MARGIN.toPx)
                }

                recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun changeMountainsPos(rvFullSize: Float) {
        val mountainsY = binding.mountains.y - EXTRA_MARGIN_MOUNTAINS.toPx
        val delta = rvFullSize - mountainsY
        val currentMargin = binding.mountains.marginBottom
        val newMargin = currentMargin - delta
        setMountainsBottomMargin(newMargin.toInt())
    }

    private fun setMountainsBottomMargin(bottomMargin: Int) {
        val params = (binding.mountains.layoutParams as ViewGroup.MarginLayoutParams)
        params.updateMargins(bottom = bottomMargin)
        binding.mountains.requestLayout()
        val mountainsAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.appearance_mountains)
        binding.mountains.startAnimation(mountainsAnimation)
    }

    private fun getCheckInData() {
        viewModel.getCheckIns()
        scope = CoroutineScope(Dispatchers.IO).also { scope ->
            scope.launch {
                viewModel.checkInData.collect{checkIns ->
                    withContext(Dispatchers.Main) {
                        adapter.setItemsList(checkIns)
                        mountainsMarginCorrect()
                    }
                }
            }
        }
    }


    private fun setClickListener() {
    }

    private fun stubClickListener() {
        with(binding) {

//            settings.setOnClickListener {
//                findNavController().navigate(R.id.action_home_to_setting)
//            }
//            statistic.setOnClickListener {
//                findNavController().navigate(R.id.action_home_to_statistics)
//            }
//
//            openCheckIn.setOnClickListener {
//                findNavController().navigate(R.id.action_home_to_details)
//            }
            delAll.setOnClickListener {
                viewModel.deleteAll()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        scope.cancel()
    }

    companion object {
        const val EXTRA_MARGIN_MOUNTAINS = 19
        const val DEFAULT_MOUNTAINS_MARGIN = 72
    }
}