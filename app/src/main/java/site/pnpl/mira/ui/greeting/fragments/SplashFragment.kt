package site.pnpl.mira.ui.greeting.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import pl.droidsonroids.gif.GifDrawable
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.domain.SettingsProvider
import site.pnpl.mira.databinding.FragmentSplashBinding
import site.pnpl.mira.domain.EmotionCreator
import site.pnpl.mira.domain.LoadingState
import site.pnpl.mira.ui.MainActivity
import site.pnpl.mira.ui.customview.PopUpDialog
import javax.inject.Inject

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var _binding: FragmentSplashBinding? = null
    private val binding: FragmentSplashBinding get() = _binding!!

    @Inject
    lateinit var settingsProvider: SettingsProvider
    @Inject
    lateinit var emotionCreator: EmotionCreator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)
        _binding = FragmentSplashBinding.bind(view)

        startAnimation()
    }

    private fun startAnimation() {
        val gifDrawable = GifDrawable(resources, R.drawable.gif_logo).apply {
            //Вызывается после каждого цикла проигрывания GIF
            addAnimationListener { completedAnimationLoop ->
                if (completedAnimationLoop == 0) {
                    when (emotionCreator.loadingState) {
                        is LoadingState.Loading -> {}
                        is LoadingState.Success -> navigate()
                        is LoadingState.Error ->  {
                            this.stop()
                            displayErrorPopUp()
                        }
                    }
                }
            }
        }
        binding.animation.setImageDrawable(gifDrawable)
    }

    private fun displayErrorPopUp() {
        val leftButtonClickListener = object : PopUpDialog.PopUpDialogClickListener {
            override fun onClick(popUpDialog: PopUpDialog) {
                popUpDialog.dismiss()
                emotionCreator.update()
                startAnimation()
            }
        }

        val rightButtonClickListener = object : PopUpDialog.PopUpDialogClickListener {
            override fun onClick(popUpDialog: PopUpDialog) {
                popUpDialog.dismiss()
                (requireActivity() as MainActivity).closeApp()
            }
        }

        PopUpDialog.Builder()
            .title(getString(R.string.pop_up_error_loading))
            .content(getString(R.string.pop_up_loading_body_3))
            .leftButtonText(getString(R.string.update))
            .rightButtonText(getString(R.string.button_close))
            .leftButtonListener(leftButtonClickListener)
            .rightButtonListener(rightButtonClickListener)
            .animationType(PopUpDialog.AnimationType.RIGHT)
            .build()
            .show(childFragmentManager, PopUpDialog.TAG)
    }

    private fun navigate() {
        if (settingsProvider.isFirstLaunch()) {
            findNavController().navigate(R.id.action_splashFragment2_to_greeting_graph)
        } else {
            findNavController().navigate(R.id.action_splash_to_navigation_home)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
