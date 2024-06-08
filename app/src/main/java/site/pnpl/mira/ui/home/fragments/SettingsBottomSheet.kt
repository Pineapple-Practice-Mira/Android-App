package site.pnpl.mira.ui.home.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentBottomSheetSettingsBinding

class SettingsBottomSheet : BottomSheetDialogFragment(R.layout.fragment_bottom_sheet_settings) {

    private var _binding: FragmentBottomSheetSettingsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBottomSheetSettingsBinding.bind(view)

        val url = arguments?.getString(TAG)

        binding.progressLoading.isVisible = true

        if (url != null) {
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.loadUrl(url)

            (dialog as BottomSheetDialog).behavior.state = STATE_COLLAPSED

            val gestureDetector = GestureDetector(binding.webView.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {

                    if (!binding.webView.canScrollVertically(-1) && distanceY < 0) {
                        val behavior = (dialog as BottomSheetDialog).behavior

                        if (behavior.state == STATE_EXPANDED) {
                            behavior.state = STATE_COLLAPSED
                        } else if (behavior.state == STATE_COLLAPSED) {
                            behavior.state = STATE_HIDDEN
                        }
                    }
                    return false
                }
            })

            binding.webView.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                false
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}