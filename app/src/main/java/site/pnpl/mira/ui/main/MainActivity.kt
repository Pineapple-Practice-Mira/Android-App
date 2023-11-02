package site.pnpl.mira.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import site.pnpl.mira.R
import site.pnpl.mira.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val primaryNavHostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = primaryNavHostFragment.navController

    }

}