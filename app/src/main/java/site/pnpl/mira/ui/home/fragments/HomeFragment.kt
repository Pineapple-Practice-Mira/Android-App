package site.pnpl.mira.ui.home.fragments

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.databinding.FragmentHomeBinding
import site.pnpl.mira.model.CheckInUI
import site.pnpl.mira.ui.check_in.fragments.CheckInDetailsFragment
import site.pnpl.mira.ui.check_in.fragments.CheckInDetailsFragment.Companion.LIST_OF_CHECK_IN_KEY
import site.pnpl.mira.ui.check_in.fragments.CheckInDetailsFragment.Companion.POSITION_KEY
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_HOME
import site.pnpl.mira.ui.check_in.fragments.CheckInSavedFragment.Companion.CALLBACK_KEY
import site.pnpl.mira.ui.home.HomeViewModel
import site.pnpl.mira.ui.home.customview.ActionBar
import site.pnpl.mira.ui.home.customview.BottomBar
import site.pnpl.mira.ui.home.customview.BottomBar.Companion.HOME
import site.pnpl.mira.ui.home.recycler_view.ChangeExpandedListener
import site.pnpl.mira.ui.home.recycler_view.CheckInAdapter
import site.pnpl.mira.ui.home.recycler_view.CheckInAdapter.Companion.TYPE_ITEM_VOID
import site.pnpl.mira.ui.home.recycler_view.ItemClickListener
import site.pnpl.mira.ui.home.recycler_view.ItemTouchHelperCallback
import site.pnpl.mira.ui.home.recycler_view.SelectedItemsListener
import site.pnpl.mira.ui.home.recycler_view.TopSpacingItemDecoration
import site.pnpl.mira.utils.OFFSET_DAYS_FOR_DEFAULT_PERIOD
import site.pnpl.mira.utils.PopUpDialog
import site.pnpl.mira.utils.screenHeight
import site.pnpl.mira.utils.toPx
import java.util.Calendar
import javax.inject.Inject


class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private var adapter: CheckInAdapter? = null
    private lateinit var itemTouchHelperCallback: ItemTouchHelperCallback

    private val viewModel: HomeViewModel by viewModels()

    private var isSelectAll = false

    @Inject
    lateinit var settingsProvider: SettingsProvider

    private var startPeriod = 0L
    private var endPeriod = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        App.instance.appComponent.inject(this)

//        @Suppress("DEPRECATION")
//        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
        setStatusBarColor()
        setPeriod()
        initActionBar()

        initBottomBar()
        initText()
        initRecyclerView()
        setClickListeners()
        getCheckInData()
    }

    private fun setStatusBarColor() {
        if (requireActivity().window.statusBarColor == ContextCompat.getColor(requireContext(), R.color.dark_grey)) {
            binding.alphaView.alpha = 1f
            binding.alphaView.animate()
                .alpha(0f)
                .setDuration(CHANGE_ALPHA_DURATION)
                .start()
            tintSystemBars()
        }
    }

    private fun setPeriod() {
        setFragmentResultListener(SELECTED_PERIOD) { requestKey, bundle ->
            startPeriod = bundle.getLong(KEY_START_PERIOD)
            endPeriod = bundle.getLong(KEY_END_PERIOD)
            clearFragmentResult(SELECTED_PERIOD)
        }

        if (startPeriod == 0L && endPeriod == 0L) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
            setDefaultPeriod()
        }
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
        with(binding.actionBar) {
            //Выбранный период для календаря
            initDatePicker(startPeriod, endPeriod)
            //Активны ли кнопки
            enableActionBarButtons(settingsProvider.isMakeFirstCheckIn())
            //Слушатель нажатия кнопок
            setActionBarClickListener { button ->
                when (button) {
                    ActionBar.Button.CALENDAR -> {}

                    ActionBar.Button.STATISTIC -> {
//                    createFakeCheckIns()
                        navigateToStatistic()
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

            //Слушать выбора периода на календаре
            setCalendarPeriodSelectionListener(childFragmentManager) { period ->
                startPeriod = period.first
                endPeriod = period.second
                getCheckInData()
            }
            //Установка текста выбранного периода
            setSelectedPeriod(androidx.core.util.Pair(startPeriod, endPeriod))
        }
    }

    private fun getCheckInData() {
        viewModel.getCheckInForPeriod(startPeriod, endPeriod)
        viewModel.onSaveEvent().observe(viewLifecycleOwner) { event ->

            val checkIns = event.contentIfNotHandled
            if (checkIns != null) {
                adapter!!.setItemsList(checkIns)
                recyclerView.scheduleLayoutAnimation()
//                mountainsMarginCorrect()
                animateMountains()
                binding.labelInfo.isVisible = checkIns.isEmpty()
            }

        }
    }

    private fun animateMountains() {
        recyclerView.doOnLayout {
            recyclerView.measure(View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY), View.MeasureSpec.UNSPECIFIED)
            val rvHeight = recyclerView.measuredHeight
            val rvFullSize = rvHeight + recyclerView.y
            if (rvFullSize < screenHeight - DEFAULT_MOUNTAINS_MARGIN.toPx) {
                val targetY = if (rvFullSize < screenHeight - (binding.mountains.height).toFloat()) {
                    screenHeight - (binding.mountains.height).toFloat()
                } else {
                    rvFullSize + EXTRA_MARGIN_MOUNTAINS.toPx
                }
                binding.mountains.animate()
                    .y(targetY)
                    .setDuration(300)
                    .start()

            } else if (binding.mountains.y < rvFullSize) {
                binding.mountains.animate()
                    .y(screenHeight.toFloat())
                    .setDuration(300)
                    .start()
            }
        }
    }

    private fun navigateToStatistic() {
        val extras = FragmentNavigatorExtras(
            binding.actionBar to "statisticActionBar"
        )

        val periods = binding.actionBar.currentPeriod!!
        val startPeriod = periods.first
        val endPeriod = periods.second

        findNavController().navigate(
            R.id.action_home_to_statistics,
            bundleOf(
                Pair(KEY_START_PERIOD, startPeriod),
                Pair(KEY_END_PERIOD, endPeriod),
            ),
            null,
            extras
        )
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
                animateMountains()
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
            .animationType(PopUpDialog.AnimationType.RIGHT)
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
        val onItemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {
                val checkIns = adapter!!.checkIns

                //Убираем пустой элемент
                if (checkIns[0].typeItem == TYPE_ITEM_VOID) {
                    checkIns.removeAt(0)
                }

                //сортировка в обратном порядке
                checkIns.sortBy { it.createdAtLong }
                val posInNewList = checkIns.size - position
                navigateToCheckInDetails(posInNewList, checkIns)
            }

        }
        adapter = CheckInAdapter(isExpanded, onChangeExpandedListener, onSelectedItemsListener, onItemClickListener)
        recyclerView.adapter = adapter
        adapter!!.setItemsList(items)
        itemTouchHelperCallback.isExpanded = isExpanded

        binding.actionBar.setRemoveMode(isExpanded)
        //Меньше 2ух так как в списке есть всегда 1 элемент - CheckIn Void
        binding.labelInfo.isVisible = adapter!!.checkIns.size < 2
    }

    private fun navigateToCheckInDetails(position: Int, checkIns: List<CheckInUI>) {
        findNavController()
            .navigate(
                R.id.action_home_to_details,
                bundleOf(
                    Pair(CheckInDetailsFragment.CALLBACK_KEY, CheckInDetailsFragment.CALLBACK_HOME),
                    Pair(POSITION_KEY, position),
                    Pair(LIST_OF_CHECK_IN_KEY, checkIns)
                )
            )
    }

    private fun setClickListeners() {
        binding.root.doOnLayout {
            binding.settings.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_setting)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
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

    private fun tintSystemBars() {
        val statusBarStartColor = ContextCompat.getColor(requireContext(), R.color.dark_grey)
        val statusBarEndColor = ContextCompat.getColor(requireContext(), R.color.white)

        ValueAnimator.ofFloat(0f, 1f)
            .apply {
                duration = CHANGE_ALPHA_DURATION
                addUpdateListener {
                    requireActivity().window.statusBarColor =
                        blendColors(statusBarStartColor, statusBarEndColor, animatedFraction)
                }
            }
            .start()
    }

    private fun blendColors(from: Int, to: Int, ratio: Float): Int {
        val inverseRatio = 1f - ratio
        val r = Color.red(to) * ratio + Color.red(from) * inverseRatio
        val g = Color.green(to) * ratio + Color.green(from) * inverseRatio
        val b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio
        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }

    companion object {
        const val EXTRA_MARGIN_MOUNTAINS = 19
        const val DEFAULT_MOUNTAINS_MARGIN = 72
        const val KEY_START_PERIOD = "START_PERIOD"
        const val KEY_END_PERIOD = "END_PERIOD"
        const val SELECTED_PERIOD = "SELECTED_PERIOD"
        const val CHANGE_ALPHA_DURATION = 700L
    }
}