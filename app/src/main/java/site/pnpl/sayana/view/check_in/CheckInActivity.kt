package site.pnpl.sayana.view.check_in

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import site.pnpl.sayana.databinding.ActivityCheckInBinding

class CheckInActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}