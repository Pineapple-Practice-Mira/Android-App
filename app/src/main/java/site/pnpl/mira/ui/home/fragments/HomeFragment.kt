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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.domain.SelectedPeriod
import site.pnpl.mira.domain.SettingsProvider
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
import site.pnpl.mira.ui.statistic.fragments.StatisticsFragment.Companion.KEY_TRANSITION
import site.pnpl.mira.ui.statistic.fragments.StatisticsFragment.Companion.KEY_TRANSITION_HOME
import site.pnpl.mira.utils.PopUpDialog
import site.pnpl.mira.ui.extensions.screenHeight
import site.pnpl.mira.ui.extensions.toPx
import javax.inject.Inject


class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private var adapter: CheckInAdapter? = null
    private lateinit var itemTouchHelperCallback: ItemTouchHelperCallback

    private val viewModel: HomeViewModel by viewModels()

    private var isSelectAll = false

    @Inject lateinit var settingsProvider: SettingsProvider
    @Inject lateinit var selectedPeriod: SelectedPeriod

    /**
     * Флаг, если фрагмент не убивался - отправляем запрос в БД в onStart, если первый инстанс в onViewCreated
     */
    private var isUpdateInOnCreate = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        App.instance.appComponent.inject(this)

        setStatusBarColor()
        initActionBar()
        initBottomBar()
        initText()
        initRecyclerView()
        setClickListeners()
        getCheckInData().also {
            isUpdateInOnCreate = true
        }
        setViewModelListener()
    }

    /**
     * Метод анимации "появления" экрана при переходе с экранов с темный беграундом
     */
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

    /**
     * Метод для анимации смены фона статус бара
     */
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

    /**
     * Метод для плавного перехода цвета
     */
    private fun blendColors(from: Int, to: Int, ratio: Float): Int {
        val inverseRatio = 1f - ratio
        val r = Color.red(to) * ratio + Color.red(from) * inverseRatio
        val g = Color.green(to) * ratio + Color.green(from) * inverseRatio
        val b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio
        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }

    private fun initActionBar() {
        with(binding.actionBar) {
            //Выбранный период для календаря
            initDatePicker(selectedPeriod.startPeriod, selectedPeriod.endPeriod)
            enableActionBarButtons(settingsProvider.isMakeFirstCheckIn())
            setActionBarClickListener { button ->
                when (button) {
                    ActionBar.Button.CALENDAR -> {}

                    ActionBar.Button.STATISTIC -> {
                        navigateToStatistic()
                    }

                    ActionBar.Button.SELECT_ALL -> {
                        isSelectAll = !isSelectAll
                        adapter!!.selectAll(isSelectAll)
                    }

                    ActionBar.Button.DELETE -> {
                        showDeletePopUpDialog()
                    }
                }
            }

            setCalendarPeriodSelectionListener(childFragmentManager) { period ->
                selectedPeriod.startPeriod = period.first
                selectedPeriod.endPeriod = period.second
                getCheckInData()
            }
        }
    }

    /**
     *  Метод перехода на фрагмент статистики. В качестве extras передаем actionBar для shared element transition
     */

    private fun navigateToStatistic() {
        val extras = FragmentNavigatorExtras(
            binding.actionBar to "statisticActionBar"
        )
        findNavController().navigate(
            R.id.action_home_to_statistics,
            bundleOf(
                Pair(KEY_TRANSITION, KEY_TRANSITION_HOME)
            ),
            null,
            extras
        )
    }

    /**
     * Метод создания и показа диалога перед удалением чекинов
     */
    private fun showDeletePopUpDialog() {
        val popUpDialogClickListenerLeft = object : PopUpDialog.PopUpDialogClickListener {
            override fun onClick(popUpDialog: PopUpDialog) {
                popUpDialog.dismiss()
            }
        }

        val popUpDialogClickListenerRight = object : PopUpDialog.PopUpDialogClickListener {
            override fun onClick(popUpDialog: PopUpDialog) {
                deleteCheckIns()
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

    /**
     * Метод удаления выбранных чекинов
     */
    private fun deleteCheckIns() {
        val items = adapter!!.getSelectedItemsAndDelete()
        viewModel.deleteListOfCheckIns(items)
        if (isSelectAll) isSelectAll = false
        initPropertyRV()
        animateMountains()
    }

    /**
     * Метод для запроса чекинов с БД.
     */
    private fun getCheckInData() {
        viewModel.getCheckInForPeriod(selectedPeriod.startPeriod, selectedPeriod.endPeriod)
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

    private val onChangeExpandedListener = object : ChangeExpandedListener {
        override fun expandAll(value: Boolean) {
            initPropertyRV(value)
        }
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView.apply {
            addItemDecoration(TopSpacingItemDecoration(ITEM_DECORATOR_SIZE))
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
            override fun notify(isHaveSelected: Boolean, allItemSelected: Boolean) {
                binding.actionBar.deleteButtonEnable(isHaveSelected)
                if (allItemSelected) {
                    isSelectAll = true
                    binding.actionBar.selectSelector(true)
                } else {
                    isSelectAll = false
                    binding.actionBar.selectSelector(false)
                }
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
        adapter!!.setItemsList(items.toMutableList())
        itemTouchHelperCallback.isExpanded = isExpanded

        setRemoveMode(isExpanded)

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

    private fun setRemoveMode(value: Boolean) {
        with(binding) {
            actionBar.setRemoveMode(value)
            if (value) {
                changeAlphaAnimation(settings, REMOVE_MODE_ACTIVE_ALPHA)
                changeAlphaAnimation(hi, REMOVE_MODE_ACTIVE_ALPHA)
                changeAlphaAnimation(name, REMOVE_MODE_ACTIVE_ALPHA)
            } else {
                changeAlphaAnimation(settings, REMOVE_MODE_INACTIVE_ALPHA)
                changeAlphaAnimation(hi, REMOVE_MODE_INACTIVE_ALPHA)
                changeAlphaAnimation(name, REMOVE_MODE_INACTIVE_ALPHA)
            }
        }
    }

    private fun changeAlphaAnimation(view: View, targetAlpha: Float) {
        view.animate()
            .alpha(targetAlpha)
            .setDuration(REMOVE_MODE_CHANGE_ALPHA_DURATION)
            .start()
    }

    private fun setClickListeners() {
        binding.root.doOnLayout {
            binding.settings.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_setting)
            }
            binding.hi.setOnClickListener {
                createFakeCheckIns()
            }
        }
    }

    private fun setViewModelListener() {
        viewModel.onSaveEvent().observe(viewLifecycleOwner) { event ->

            val checkIns = event.contentIfNotHandled
            if (checkIns != null) {
                lifecycleScope.launch {
                    adapter!!.setItemsList(checkIns.toMutableList())
                    recyclerView.scheduleLayoutAnimation()
                    animateMountains()
                }
                binding.labelInfo.isVisible = checkIns.isEmpty()
            }
        }

        viewModel.countCheckIns.observe(viewLifecycleOwner) { count ->
            if (count == 0L) {
                binding.actionBar.enableActionBarButtons(false)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!isUpdateInOnCreate) {
            getCheckInData()
        }
    }

    override fun onResume() {
        super.onResume()
        animateMountains()
    }

    /**
     * Метод для анимации гор. Двиагаем горы:
     * - если размер recyclerView + его позиция по Y меньше, чем максимальная позиция для гор
     * - если горы выше чем recyclerView
     */
    private fun animateMountains() {
        recyclerView.doOnLayout {
            val rvFullSize = recyclerView.computeVerticalScrollRange() + recyclerView.y
            if (rvFullSize < screenHeight - DEFAULT_MOUNTAINS_MARGIN.toPx) {
                val targetY = if (rvFullSize < screenHeight - (binding.mountains.height).toFloat()) {
                    screenHeight - (binding.mountains.height).toFloat()
                } else {
                    rvFullSize + EXTRA_MARGIN_MOUNTAINS.toPx
                }
                startMountainsAnimation(targetY)

            } else if (binding.mountains.y < rvFullSize) {
                startMountainsAnimation(screenHeight.toFloat())
            }
        }
    }

    private fun startMountainsAnimation(targetY: Float) {
        binding.mountains.animate()
            .y(targetY)
            .setDuration(MOUNTAINS_ANIMATION_DURATION)
            .start()
    }

    private fun createFakeCheckIns() {
        viewModel.insertListOfCheckIns()
        Toast.makeText(requireContext(), "Чек-ины добавлены! Перезайди на страницу", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        isUpdateInOnCreate = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ITEM_DECORATOR_SIZE = 12
        const val EXTRA_MARGIN_MOUNTAINS = 19
        const val DEFAULT_MOUNTAINS_MARGIN = 72
        const val CHANGE_ALPHA_DURATION = 700L
        const val MOUNTAINS_ANIMATION_DURATION = 300L

        const val REMOVE_MODE_CHANGE_ALPHA_DURATION = 300L
        const val REMOVE_MODE_ACTIVE_ALPHA = 0.3f
        const val REMOVE_MODE_INACTIVE_ALPHA = 1f
    }
}