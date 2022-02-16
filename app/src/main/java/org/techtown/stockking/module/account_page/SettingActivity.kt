package org.techtown.stockking.module.account_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.techtown.stockking.R
import org.techtown.stockking.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val binding = ActivitySettingBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener{
            finish()
        }
    }
}