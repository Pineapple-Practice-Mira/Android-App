package site.pnpl.mira.ui.greeting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import site.pnpl.mira.ui.greeting.fragments.AcquaintanceFragment
import site.pnpl.mira.utils.HideNavigationBars
import site.pnpl.sayana.R
import site.pnpl.sayana.databinding.ActivityGreetingBinding

class GreetingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGreetingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGreetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startFragment(AcquaintanceFragment())
        HideNavigationBars.hide(window, binding.root)

    }

    fun startFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentPlaceholder, fragment)
            .commit()
    }
}