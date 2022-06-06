package org.techtown.stockking

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import org.techtown.stockking.adapter.ViewPagerAdapter
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.ActivityMainBinding
import org.techtown.stockking.fragment.TopListFragment
import org.techtown.stockking.module.login.LoginActivity
import org.techtown.stockking.network.ApiWrapperLogin


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApiWrapperLogin.getAutoLogin(MySharedPreferences.getToken(this)){
            if(it == null){
                val intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else if(it.isSuccess){

            }
        }
        binding.viewpager.adapter = ViewPagerAdapter(this)
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}
            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.menu.getItem(position).isChecked=true
            }
        })

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_one -> {
                    binding.viewpager.currentItem = 0
                }
                R.id.item_two -> {
                    binding.viewpager.currentItem = 1
                }
                R.id.item_three -> {
                    binding.viewpager.currentItem = 2
                }
                else -> {
                    false
                }
            }
            true
        }

    }

    fun viewpagerStart(){

    }

    override fun onStart() {
        super.onStart()


    }
}