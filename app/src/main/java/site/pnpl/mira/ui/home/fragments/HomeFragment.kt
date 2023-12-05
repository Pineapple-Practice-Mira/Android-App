package site.pnpl.mira.ui.home.fragments

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionListenerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.transition.doOnEnd
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.SelectedPeriod
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
import site.pnpl.mira.ui.home.recycler_view.CheckInAdapterNew
import site.pnpl.mira.ui.home.recycler_view.ItemClickListener
import site.pnpl.mira.ui.home.recycler_view.ItemTouchHelperCallback
import site.pnpl.mira.ui.home.recycler_view.SelectedItemsListener
import site.pnpl.mira.ui.home.recycler_view.TopSpacingItemDecoration
import site.pnpl.mira.ui.statistic.fragments.StatisticsFragment
import site.pnpl.mira.utils.PopUpDialog
import site.pnpl.mira.utils.TranslationListener
import site.pnpl.mira.utils.screenHeight
import site.pnpl.mira.utils.toPx
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

    @Inject
    lateinit var selectedPeriod: SelectedPeriod
    private lateinit var adapterNew: CheckInAdapterNew

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = StatisticsFragment.ANIMATION_TRANSITION_DURATION
            addListener(object : TranslationListener() {
                override fun onTransitionEnd(transition: androidx.transition.Transition) {
                    println("onTransitionEnd")
                }
            })
        }
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

        setStatusBarColor()
        initActionBar()
        initBottomBar()
        initText()
//        initRecyclerView()
        initRecyclerViewNew()
        setClickListeners()
        getCheckInData()
        setViewModelListener()

    }

    override fun onResume() {
        super.onResume()
        view?.animation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                getCheckInsByPeriod()
            }
        })
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
                        showPopUpDialog()
                    }
                }
            }

            setCalendarPeriodSelectionListener(childFragmentManager) { period ->
                selectedPeriod.startPeriod = period.first
                selectedPeriod.endPeriod = period.second
                getCheckInsByPeriod()
//                getCheckInData()
            }
        }
    }

    private fun getCheckInData() {
//        viewModel.getCheckInForPeriod(selectedPeriod.startPeriod, selectedPeriod.endPeriod)
//        enableProgressBar(true)
    }

    private fun initRecyclerViewNew() {
        adapterNew = CheckInAdapterNew()
        adapterNew.addOnPagesUpdatedListener {
            lifecycleScope.launch(Dispatchers.Main) {
                recyclerView.scheduleLayoutAnimation()
                binding.labelInfo.isVisible = adapterNew.itemCount == 0
                animateMountains()
            }

        }
        recyclerView = binding.recyclerView.apply {
            adapter = adapterNew
            addItemDecoration(TopSpacingItemDecoration(12))
        }
    }

    private fun setViewModelListener() {
//        viewModel.onSaveEvent().observe(viewLifecycleOwner) { event ->
//
//            val checkIns = event.contentIfNotHandled
//            if (checkIns != null) {
//                lifecycleScope.launch {
//                    withContext(Dispatchers.Main) {
//                        adapter!!.setItemsList(checkIns)
//                        recyclerView.scheduleLayoutAnimation()
//                        animateMountains()
//                    }
//                }
//                binding.labelInfo.isVisible = checkIns.isEmpty()
//            }
//        enableProgressBar(false)
//        }
//        viewModel.isLoaded.observe(viewLifecycleOwner) {
//            enableProgressBar(it)
//        }
    }

    private fun getCheckInsByPeriod() {
        adapterNew.submitData(lifecycle, PagingData.empty())
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getCheckInForPeriod(selectedPeriod.startPeriod, selectedPeriod.endPeriod)
                .collect {
                    adapterNew.submitData(it)
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

        findNavController().navigate(
            R.id.action_home_to_statistics,
            null,
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
            binding.hi.setOnClickListener {
                createFakeCheckIns()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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
                doOnEnd {
                    getCheckInsByPeriod()
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

    private fun enableProgressBar(value: Boolean) {
        binding.haze.isVisible = value
        binding.progressBar.isVisible = value
    }

    companion object {
        const val EXTRA_MARGIN_MOUNTAINS = 19
        const val DEFAULT_MOUNTAINS_MARGIN = 72
        const val CHANGE_ALPHA_DURATION = 300L
    }
}