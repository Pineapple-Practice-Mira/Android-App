package site.pnpl.mira.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import site.pnpl.mira.R
import site.pnpl.mira.databinding.ActivityMainBinding
import site.pnpl.mira.utils.HideNavigationBars

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L
    private val navHostFragment:  NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        HideNavigationBars.hide(window, binding.root)

    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navHostFragment.childFragmentManager.backStackEntryCount == 0) {
                    exitDoubleTap()
                } else {
                    findNavController(R.id.navHost).popBackStack()
                }
            }
        }

    private fun exitDoubleTap() {
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finish()
        } else {
            Toast.makeText(this, resources.getString(R.string.alert_double_tap_exit), Toast.LENGTH_SHORT).show()
        }
        backPressed = System.currentTimeMillis()

    }

    companion object {
        const val TIME_INTERVAL = 2000
    }
}