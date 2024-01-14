package site.pnpl.mira.ui.statistic.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.domain.SelectedPeriod
import site.pnpl.mira.databinding.FragmentStatisticsByFactorBinding
import site.pnpl.mira.models.CheckInUI
import site.pnpl.mira.models.FactorData
import site.pnpl.mira.models.FactorsList
import site.pnpl.mira.ui.check_in.fragments.CheckInDetailsFragment
import site.pnpl.mira.ui.home.customview.ActionBar
import site.pnpl.mira.ui.statistic.StatisticByFactorViewModel
import site.pnpl.mira.ui.statistic.fragments.StatisticsFragment.Companion.KEY_FACTOR_DATA
import site.pnpl.mira.ui.statistic.fragments.StatisticsFragment.Companion.KEY_TRANSITION
import site.pnpl.mira.ui.statistic.fragments.StatisticsFragment.Companion.KEY_TRANSITION_STATISTIC_BY_FACTOR
import site.pnpl.mira.ui.statistic.recycler_view.BottomSpacingItemDecoration
import site.pnpl.mira.ui.statistic.recycler_view.CheckInStatisticAdapter
import javax.inject.Inject

class StatisticsByFactorFragment : Fragment(R.layout.fragment_statistics_by_factor) {
    private var _binding: FragmentStatisticsByFactorBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CheckInStatisticAdapter
    private val viewModel: StatisticByFactorViewModel by viewModels()

    @Inject lateinit var selectedPeriod: SelectedPeriod
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStatisticsByFactorBinding.inflate(layoutInflater, container, false)

        sharedElementEnterTransition = ChangeBounds().apply {
            duration = StatisticsFragment.ANIMATION_TRANSITION_DURATION
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = StatisticsFragment.ANIMATION_TRANSITION_DURATION
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

        val fd: FactorData? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            savedInstanceState?.getParcelable(KEY_SAVED_FACTOR_DATA, FactorData::class.java)
        } else {
            @Suppress("DEPRECATION")
            savedInstanceState?.getParcelable(KEY_SAVED_FACTOR_DATA)
        }
        if (fd != null) {
            factorData = fd
        }

        initActionBar()
        setClickListeners()
        initRecyclerView()
        viewModelListener()
        createFactorAnalysisView()

        initText()
        getCountCheckInsByFactor()
        getCheckIns()
    }

    private fun getCheckIns() {
        viewModel.getCheckInForPeriodByFactorId(
            selectedPeriod.startPeriod,
            selectedPeriod.endPeriod,
            factorData!!.factorId
        )
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        adapter = CheckInStatisticAdapter(onItemClickListener)
        recyclerView.apply {
            adapter = this@StatisticsByFactorFragment.adapter
            addItemDecoration(BottomSpacingItemDecoration(12))
        }
    }

    private fun viewModelListener() {
        viewModel.onGetEvent().observe(viewLifecycleOwner) { event ->
            val checkIns = event.contentIfNotHandled
            if (checkIns != null) {
                adapter.setItemList(checkIns)
                binding.counterPeriod.text = adapter.checkIns.size.toString()
                recyclerView.scheduleLayoutAnimation()
            }
        }

        viewModel.countCheckIns.observe(viewLifecycleOwner) { totalCount ->
            binding.counterTotal.text = totalCount.toString()
        }
    }

    private val onItemClickListener = object : CheckInStatisticAdapter.OnItemClickListener {
        override fun onClick(position: Int) {
            val checkIns = adapter.checkIns

            //сортировка в обратном порядке
            checkIns.sortBy { it.createdAtLong }
            val posInNewList = (checkIns.size - 1) - position

            navigateToCheckInDetails(posInNewList, checkIns)
        }

    }

    private fun navigateToCheckInDetails(position: Int, checkIns: MutableList<CheckInUI>) {
        findNavController()
            .navigate(
                R.id.action_stat_by_factor_to_details,
                bundleOf(
                    Pair(CheckInDetailsFragment.CALLBACK_KEY, CheckInDetailsFragment.CALLBACK_STATISTIC),
                    Pair(CheckInDetailsFragment.POSITION_KEY, position),
                    Pair(CheckInDetailsFragment.LIST_OF_CHECK_IN_KEY, checkIns)
                )
            )
    }

    private fun initActionBar() {
        with(binding.actionBar) {
            initDatePicker(selectedPeriod.startPeriod, selectedPeriod.endPeriod)
            setDisplayMode(ActionBar.DisplayMode.STATISTIC_SECOND)
        }
    }

    private fun initText() {
        val factorName = getString(FactorsList.factors[factorData!!.factorId].nameResId)
        val text = " ${getString(R.string.statistic_factor_name, factorName)}"
        binding.factorName.apply {
            this.text = text
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(ANIMATION_DURATION_ALPHA_TEXT)
                .start()
        }
    }

    private fun setClickListeners() {
        binding.back.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.title to "title"
            )
            findNavController().navigate(
                R.id.action_stat_by_factor_to_statistic,
                bundleOf(
                    Pair(KEY_TRANSITION, KEY_TRANSITION_STATISTIC_BY_FACTOR)
                ),
                null,
                extras
            )
        }
    }

    private fun createFactorAnalysisView() {
        val fd = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            findNavController().currentBackStackEntry?.arguments?.getParcelable(KEY_FACTOR_DATA, FactorData::class.java)
        } else {
            @Suppress("DEPRECATION")
            findNavController().currentBackStackEntry?.arguments?.getParcelable(KEY_FACTOR_DATA)
        }

        fd?.let {
            factorData = it
            binding.factorAnalysisView.apply {
                init(
                    it.positiveCount,
                    it.negativeCount,
                    getString(it.nameIdRes)
                )
                startAnimation(1)
            }
        }
    }

    private fun getCountCheckInsByFactor() {
        viewModel.getCountCheckInsByFactor(factorData!!.factorId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_SAVED_FACTOR_DATA, factorData)
        super.onSaveInstanceState(outState)
    }

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//         val factorData: FactorData? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//             savedInstanceState?.getParcelable(KEY_SAVE_FACTOR_DATA, FactorData::class.java)
//         } else {
//             savedInstanceState?.getParcelable(KEY_SAVE_FACTOR_DATA)
//         }
//        if (factorData != null) {
//            this.factorData = factorData
//        }
//    }

    companion object {
        const val KEY_SAVED_FACTOR_DATA = "KEY_SAVED_FACTOR_DATA"
        const val ANIMATION_DURATION_ALPHA_TEXT = 300L
        private var factorData: FactorData? = null
    }
}