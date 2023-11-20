package site.pnpl.mira.ui.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.core.view.marginBottom
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentHomeBinding
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_HOME
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_KEY
import site.pnpl.mira.ui.customview.ActionBar
import site.pnpl.mira.ui.customview.BottomBar
import site.pnpl.mira.ui.customview.BottomBar.Companion.HOME
import site.pnpl.mira.ui.home.recycler_view.HomeAdapter
import site.pnpl.mira.ui.home.recycler_view.TopSpacingItemDecoration
import site.pnpl.mira.utils.OFFSET_DAYS_FOR_DEFAULT_PERIOD
import site.pnpl.mira.utils.toPx
import java.util.Calendar


class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter

    private val viewModel: HomeViewModel by viewModels()

    private var startPeriod = 0L
    private var endPeriod = 0L
    private lateinit var dateRangePicker: MaterialDatePicker<androidx.core.util.Pair<Long, Long>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        @Suppress("DEPRECATION")
        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
        stubClickListener()

        initActionBar()
        setDefaultPeriod()
        initBottomBar()
        initRecyclerView()
        setMountainsBottomMargin(DEFAULT_MOUNTAINS_MARGIN.toPx)
        getCheckInData()
    }

    private fun setDefaultPeriod() {
        endPeriod = MaterialDatePicker.todayInUtcMilliseconds()
        Calendar.getInstance().apply {
            timeInMillis = endPeriod
            add(Calendar.DAY_OF_YEAR, OFFSET_DAYS_FOR_DEFAULT_PERIOD)
            startPeriod = timeInMillis
        }
    }

    private fun initActionBar() {
        binding.actionBar.setActionBarClickListener { button ->
            when (button) {
                ActionBar.Button.CALENDAR -> {
                    showDatePicker()
                }

                ActionBar.Button.STATISTIC -> {
                    createFakeCheckIns()
                }

                ActionBar.Button.SELECT_ALL -> {}
                ActionBar.Button.DELETE -> {}
            }
        }
    }

    private fun createFakeCheckIns() {
        viewModel.insertListOfCheckIns()
        Toast.makeText(requireContext(), "Чек-ины добавлены! Перезайди на страницу", Toast.LENGTH_SHORT).show()
    }

    private fun showDatePicker() {

        dateRangePicker.show(childFragmentManager, DATE_PICKER_TAG)

        dateRangePicker.addOnPositiveButtonClickListener { periods ->
            binding.actionBar.setSelectedPeriod(periods)
            startPeriod = periods.first
            endPeriod = periods.second
            getCheckInData()
        }
    }

    private fun initBottomBar() {
        binding.bottomBar.setSelectedButton(HOME)
        binding.bottomBar.setBottomBarClickListener { button ->
            when (button) {
                BottomBar.Button.HOME -> {}
                BottomBar.Button.EXERCISES_LIST -> {
                    findNavController().navigate(R.id.action_home_to_exercises)
                }

                BottomBar.Button.CHECK_IN -> {
                    findNavController().navigate(R.id.createCheckIn, bundleOf(Pair(CALLBACK_KEY, CALLBACK_HOME)))
                }
            }
        }
    }


    private fun initRecyclerView() {
        adapter = HomeAdapter()
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(TopSpacingItemDecoration(12))
    }

    private fun mountainsMarginCorrect() {
        recyclerView.doOnLayout {
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
        }
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
        viewModel.getCheckInForPeriod(startPeriod, endPeriod)
        viewModel.checkInLiveData.observe(viewLifecycleOwner) { checkIns ->
            println("checkIns.size - ${checkIns.size}")
            adapter.setItemsList(checkIns)
            mountainsMarginCorrect()
        }
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
//            delAll.setOnClickListener {
//                viewModel.deleteAll()
//            }
        }
    }

    override fun onResume() {
        super.onResume()
        initDatePicker()
    }

    private fun initDatePicker() {
        dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setPositiveButtonText("Выбрать")
            .setTheme(R.style.MiraDatePicker)
            .setSelection(
                androidx.core.util.Pair(
                    startPeriod,
                    endPeriod
                )
            )
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setEnd(endPeriod)
                    .build()
            )
            .build()
        binding.actionBar.setSelectedPeriod(androidx.core.util.Pair(startPeriod, endPeriod))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
        const val EXTRA_MARGIN_MOUNTAINS = 19
        const val DEFAULT_MOUNTAINS_MARGIN = 72
    }
}