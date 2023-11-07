package site.pnpl.mira.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentExercisesListBinding
import site.pnpl.mira.ui.customview.BottomBar
import site.pnpl.mira.ui.home.recycler_view.CheckInItem

class ExercisesListFragment : Fragment() {
    private var _binding: FragmentExercisesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stubClickListener()

        initBottomBar()

    }

    private fun initBottomBar() {
        binding.bottomBar.setSelectedButton(BottomBar.EXERCISES_LIST)
        binding.bottomBar.setClickListener(object : BottomBar.BottomBarClicked {
            override fun onClick(button: BottomBar.BottomBarButton) {
                when (button) {
                    BottomBar.BottomBarButton.HOME -> {
                        findNavController().navigate(R.id.action_exercises_list_to_home)
                    }
                    BottomBar.BottomBarButton.EXERCISES_LIST -> {}
                    BottomBar.BottomBarButton.CHECK_IN -> {
                        App.ITEMS.add(
                            CheckInItem(
                                day = "04",
                                month = "сент",
                                dayOfWeekAndTime = "Пн, 13:00",
                                emotion = "#усталость",
                                emotionDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.emo)!!,
                                dateTime = System.currentTimeMillis()

                            )
                        )
//                        открыть чекин
//                        findNavController().navigate(R.id.action_exercises_list_to_start_check_in, bundleOf(Pair(CALLBACK_KEY, CALLBACK_EXERCISES)))
                    }
                }
            }
        })
    }

    private fun stubClickListener() {
        binding.openExercise.setOnClickListener {
            findNavController().navigate(R.id.action_exercises_list_to_exercise)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}