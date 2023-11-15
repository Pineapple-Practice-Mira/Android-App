package site.pnpl.mira.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import site.pnpl.mira.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
            return@setOnItemSelectedListener true
        }

        binding.lottie.setOnClickListener {
            binding.lottie.playAnimation()
        }
    }

}