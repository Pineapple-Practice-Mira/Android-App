package site.pnpl.mira.ui.home.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.databinding.FragmentHomeBinding
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_HOME
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_KEY
import site.pnpl.mira.ui.home.HomeViewModel
import site.pnpl.mira.ui.home.customview.ActionBar
import site.pnpl.mira.ui.home.customview.BottomBar
import site.pnpl.mira.ui.home.customview.BottomBar.Companion.HOME
import site.pnpl.mira.ui.home.recycler_view.CheckInAdapter
import site.pnpl.mira.ui.home.recycler_view.ItemTouchHelperCallback
import site.pnpl.mira.ui.home.recycler_view.TopSpacingItemDecoration
import site.pnpl.mira.utils.OFFSET_DAYS_FOR_DEFAULT_PERIOD
import site.pnpl.mira.utils.PopUpDialog
import site.pnpl.mira.utils.toPx
import java.util.Calendar
import javax.inject.Inject


class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mountainsDefPositionY by lazy {
        binding.mountains.y - EXTRA_MARGIN_MOUNTAINS.toPx
    }

    private lateinit var recyclerView: RecyclerView
    private var adapter: CheckInAdapter? = null
    private lateinit var itemTouchHelperCallback: ItemTouchHelperCallback

    private val viewModel: HomeViewModel by viewModels()

    private var startPeriod = 0L
    private var endPeriod = 0L
    private lateinit var dateRangePicker: MaterialDatePicker<androidx.core.util.Pair<Long, Long>>
    private var isSelectAll = false

    @Inject
    lateinit var settingsProvider: SettingsProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        App.instance.appComponent.inject(this)

        @Suppress("DEPRECATION")
        requireActivity().window.statusBarColor = resources.getColor(R.color.white)

        initActionBar()
        initBottomBar()
        initText()
        initRecyclerView()
        setClickListeners()
        setDefaultPeriod()
//        setMountainsBottomMargin(DEFAULT_MOUNTAINS_MARGIN.toPx)
        getCheckInData()
    }


    private fun initActionBar() {
        binding.actionBar.enableActionBarButtons(settingsProvider.isMakeFirstCheckIn())
        binding.actionBar.setActionBarClickListener { button ->
            when (button) {
                ActionBar.Button.CALENDAR -> {
                    showDatePicker()
                }

                ActionBar.Button.STATISTIC -> {
                    createFakeCheckIns()
                }

                ActionBar.Button.SELECT_ALL -> {
                    isSelectAll = !isSelectAll
                    adapter!!.selectAll(isSelectAll)
                }

                ActionBar.Button.DELETE -> {
                    showPopUpDialog()
                }
            }
        }
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

    private fun createFakeCheckIns() {
        viewModel.insertListOfCheckIns()
        Toast.makeText(requireContext(), "Чек-ины добавлены! Перезайди на страницу", Toast.LENGTH_SHORT).show()
    }

    private fun showPopUpDialog() {
        val popUpDialogClickListenerLeft = object : PopUpDialog.PopUpDialogClickListener {
            override fun onClick(popUpDialog: PopUpDialog) {
                popUpDialog.dismiss()
            }
        }


        val popUpDialogClickListenerRight = object : PopUpDialog.PopUpDialogClickListener {
            override fun onClick(popUpDialog: PopUpDialog) {
                val items = adapter!!.getSelectedItemsAndDelete()
                viewModel.deleteListOfCheckIns(items)
                if (isSelectAll) isSelectAll = false
                initPropertyRV()
                popUpDialog.dismiss()
            }

        }
        val popUpDialog = PopUpDialog.Builder()
            .title(resources.getString(R.string.pop_up_home_title))
            .content(resources.getString(R.string.pop_up_home_content))
            .leftButtonText(resources.getString(R.string.pop_up_home_left))
            .rightButtonText((resources.getString(R.string.pop_up_home_right)))
            .leftButtonListener(popUpDialogClickListenerLeft)
            .rightButtonListener(popUpDialogClickListenerRight)
            .build()
        popUpDialog.show(childFragmentManager, PopUpDialog.TAG)
    }

    private fun initBottomBar() {
        with(binding) {
            floatingArrow.isVisible = !settingsProvider.isMakeFirstCheckIn()
            bottomBar.setSelectedButton(HOME)
            bottomBar.setBottomBarClickListener { button ->
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
    }

    private fun initText() {
        val name = settingsProvider.getName()
        if (name.isNotEmpty()) {
            binding.name.text = name
            val newHi = "${binding.hi.text},"
            binding.hi.text = newHi
        }

        binding.labelInfo.text =
            if (settingsProvider.isMakeFirstCheckIn())
                resources.getString(R.string.label_info_2)
            else
                resources.getString(R.string.label_info_1)
    }

    private fun initRecyclerView() {

        recyclerView = binding.recyclerView.apply {
            addItemDecoration(TopSpacingItemDecoration(12))
        }

        itemTouchHelperCallback = ItemTouchHelperCallback(onChangeExpandedListener)
        val touchHelper = ItemTouchHelper(itemTouchHelperCallback)
        touchHelper.attachToRecyclerView(recyclerView)

        initPropertyRV()

    }

    private fun initPropertyRV(isExpanded: Boolean = false) {

        var items = emptyList<CheckInUI>()
        if (adapter != null) {
            items = adapter!!.checkIns
        }

        val onSelectedItemsListener = object : SelectedItemsListener {
            override fun notify(isHaveSelected: Boolean) {
                binding.actionBar.trashBoxEnable(isHaveSelected)
            }
        }

        adapter = CheckInAdapter(isExpanded, onChangeExpandedListener, onSelectedItemsListener)
        recyclerView.adapter = adapter
        adapter!!.setItemsList(items)
        itemTouchHelperCallback.isExpanded = isExpanded

        binding.actionBar.setRemoveMode(isExpanded)
        binding.labelInfo.isVisible = adapter!!.checkIns.size < 2
    }


    private fun mountainsMarginCorrect() {
        recyclerView.doOnLayout {
            val mountainsY = mountainsDefPositionY
            recyclerView.measure(View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY), View.MeasureSpec.UNSPECIFIED)
            val rvHeight = recyclerView.measuredHeight
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

    private fun setDefaultPeriod() {
        endPeriod = MaterialDatePicker.todayInUtcMilliseconds()
        Calendar.getInstance().apply {
            timeInMillis = endPeriod
            add(Calendar.DAY_OF_YEAR, OFFSET_DAYS_FOR_DEFAULT_PERIOD)
            startPeriod = timeInMillis
        }
    }

    private fun setClickListeners() {
        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_setting)
        }
    }

    private fun getCheckInData() {
        viewModel.getCheckInForPeriod(startPeriod, endPeriod)
        viewModel.onSaveEvent().observe(viewLifecycleOwner) { event ->

            val checkIns = event.contentIfNotHandled
            if (checkIns != null) {
                adapter!!.setItemsList(checkIns)
                mountainsMarginCorrect()
                binding.labelInfo.isVisible = checkIns.isEmpty()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        initDatePicker()
    }

    private fun initDatePicker() {
        dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(resources.getString(R.string.calendar_tittle))
            .setPositiveButtonText(resources.getString(R.string.calendar_positive_button))
            .setNegativeButtonText(resources.getString(R.string.calendar_negative_button))
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

    private val onChangeExpandedListener = object : ChangeExpandedListener {
        override fun expandAll(value: Boolean) {
            initPropertyRV(value)
        }
    }

    companion object {
        const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
        const val EXTRA_MARGIN_MOUNTAINS = 19
        const val DEFAULT_MOUNTAINS_MARGIN = 72
    }

    interface SelectedItemsListener {
        fun notify(isHaveSelected: Boolean)
    }


    interface ChangeExpandedListener {
        fun expandAll(value: Boolean)
    }
}