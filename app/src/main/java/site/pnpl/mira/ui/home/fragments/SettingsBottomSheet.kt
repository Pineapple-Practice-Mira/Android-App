package site.pnpl.mira.ui.home.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import site.pnpl.mira.R
import site.pnpl.mira.databinding.FragmentBottomSheetSettingsBinding

class SettingsBottomSheet : BottomSheetDialogFragment(R.layout.fragment_bottom_sheet_settings) {

    private var _binding: FragmentBottomSheetSettingsBinding? = null
    private val binding get() = _binding!!
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBottomSheetSettingsBinding.bind(view)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(URL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        const val TAG = "ModalBottomSheet"
        const val URL = "https://www.pnpl.site/"
    }
}