package org.techtown.stockking.module.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import org.techtown.stockking.R
import org.techtown.stockking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewpager=binding.viewpager
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_one -> {
                    viewpager.currentItem = 0
                }
                R.id.item_two -> {
                    viewpager.currentItem = 1
                }
                R.id.item_three -> {
                    viewpager.currentItem = 2
                }
                else -> {
                    false
                }
            }
            true
        }

        viewpager.adapter = ViewPagerAdapter(this)

    }
}