package site.pnpl.mira.ui.home.fragments

import android.content.ClipData
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.SpannableStringBuilder
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import site.pnpl.mira.App
import site.pnpl.mira.BuildConfig
import site.pnpl.mira.R
import site.pnpl.mira.domain.SettingsProvider
import site.pnpl.mira.databinding.FragmentSettingsBinding
import site.pnpl.mira.domain.analitycs.Analytics
import site.pnpl.mira.domain.analitycs.AnalyticsEvent
import site.pnpl.mira.utils.MIN_LENGTH_IN_INPUT_NAME
import java.io.File
import javax.inject.Inject


class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var settingsProvider: SettingsProvider
    @Inject lateinit var analytics: Analytics
    private var savedName = ""
    private var newName = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)
        App.instance.appComponent.inject(this)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.dark_grey)

        binding.save.isEnabled = false

        initInputField()
        setClickListeners()
        setVersion()
    }


    private fun initInputField() {
        binding.inputName.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.inputName.setRawInputType(InputType.TYPE_CLASS_TEXT)

        savedName = settingsProvider.getName()
        binding.inputName.apply {
            text = SpannableStringBuilder(savedName)
        }

        binding.inputName.doAfterTextChanged { editable ->
            newName = editable.toString()
            binding.save.apply {
                isEnabled = (newName.length >= MIN_LENGTH_IN_INPUT_NAME) && newName != savedName
                if (isEnabled) {
                    text = resources.getString(R.string.button_save_change)
                }
            }
        }
    }

    private fun setClickListeners() {
        with(binding) {
            close.setOnClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_SETTINGS_CLOSE)
                close.isClickable = false
                findNavController().popBackStack()
            }

            save.setOnClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_SETTINGS_SAVE_NAME)
                savedName = newName
                settingsProvider.saveName(savedName)
                save.isEnabled = false
                save.text = resources.getString(R.string.button_change_saved)
            }

            about.setOnClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_SETTINGS_ABOUT)
                aboutClicked()
            }

            share.setOnClickListener {
                analytics.sendEvent(AnalyticsEvent.NAME_SETTINGS_SHARE)
                shareClicked()
            }
        }
    }

    private fun aboutClicked() {
        val modalBottomSheet = SettingsBottomSheet()
        modalBottomSheet.show(childFragmentManager, SettingsBottomSheet.TAG)
    }

    private fun shareClicked() {
        val url = "https://mira-mobile-app.vercel.app/"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val imageUri = FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", getAssetsFile("mira2.png"))
            val share = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                putExtra(Intent.EXTRA_TITLE, "Мира!")
                data = imageUri
                clipData = ClipData.newRawUri(null, imageUri)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }, null)
            startActivity(share)

        } else {

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun getAssetsFile(fileName: String): File {
        val file = File(requireContext().filesDir, fileName)

        if (!file.exists()) {
            file.outputStream().use { outputStream ->
                requireContext().assets.open(fileName).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
        return file
    }

    private fun setVersion() {
        binding.version.text = getString(R.string.version_name, BuildConfig.VERSION_NAME)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}