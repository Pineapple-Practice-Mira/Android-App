package site.pnpl.mira.ui.check_in.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.data.database.check_in.entity.CheckIn
import site.pnpl.mira.databinding.FragmentCheckInSavedBinding
import site.pnpl.mira.domain.EmotionProvider
import site.pnpl.mira.models.FactorsList
import site.pnpl.mira.ui.check_in.customview.BubbleView
import site.pnpl.mira.ui.check_in.customview.ScrollBubblesView
import site.pnpl.mira.utils.MiraDateFormat
import javax.inject.Inject

class CheckInSavedFragment : Fragment(R.layout.fragment_check_in_saved) {

    @Inject lateinit var emotionProvider: EmotionProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

        FragmentCheckInSavedBinding.bind(view).apply {
            val key = findNavController().currentBackStackEntry?.arguments?.getString(CALLBACK_KEY)

            val checkIn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                findNavController().currentBackStackEntry?.arguments?.getParcelable(CHECK_IN_KEY, CheckIn::class.java)
            } else {
                @Suppress("DEPRECATION")
                findNavController().currentBackStackEntry?.arguments?.getParcelable(CHECK_IN_KEY)
            }

            close.setOnClickListener {
                navigateByKey(key)
            }
            later.setOnClickListener {
                navigateByKey(key)
            }
            exercises.setOnClickListener {
                //open exercise
            }
            initScrollBubbleView(scrollBubbleView, checkIn)
        }
    }

    private fun navigateByKey(key: String?) {
        when (key) {
            CALLBACK_EXERCISES -> findNavController().navigate(R.id.action_checkInCompleted_to_exercises_list)
            else -> findNavController().navigate(R.id.action_checkInCompleted_to_navigation_home)
        }
    }

    private fun initScrollBubbleView(scrollBubblesView: ScrollBubblesView, checkIn: CheckIn?) {
        checkIn?.let {
            scrollBubblesView.addListOfBubbles(
                listOf(
                    BubbleView(
                        requireContext(),
                        BubbleView.Type.RIGHT_HIGH,
                        getString(
                            R.string.bubble_5,
                            MiraDateFormat(checkIn.createdAtLong).getDayMonthYear(),
                            emotionProvider.getNameGenitive(checkIn.emotionId),
                            getString(FactorsList.factors[checkIn.factorId].nameResId)
                        )
                    ),
                    BubbleView(requireContext(), BubbleView.Type.LEFT_SMALL, getString(R.string.bubble_6))
                )
            )
        }
    }

    companion object {
        const val CALLBACK_KEY = "CALLBACK_KEY"
        const val CALLBACK_HOME = "HOME"
        const val CALLBACK_EXERCISES = "EXERCISES"
        const val CHECK_IN_KEY = "CHECK_IN"
    }
}