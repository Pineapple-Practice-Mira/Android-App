package site.pnpl.mira.ui.greeting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import site.pnpl.sayana.databinding.ActivityGreetingBinding

class GreetingActivity : AppCompatActivity() {
    lateinit var binding: ActivityGreetingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGreetingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}