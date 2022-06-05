package org.techtown.stockking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.ActivityMainBinding
import org.techtown.stockking.model.UserModel
import org.techtown.stockking.adapter.ViewPagerAdapter
import org.techtown.stockking.module.login.LoginActivity
import org.techtown.stockking.network.ApiWrapper
import org.techtown.stockking.network.ApiWrapperLogin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent=intent

        Log.i("get auto","intent : "+intent.getStringExtra("first"))
        if(intent.getStringExtra("first").isNullOrEmpty()){
            ApiWrapperLogin.getAutoLogin(MySharedPreferences.getToken(this)){
                if(it == null){
                    val intent= Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrollStateChanged(state: Int) {}
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

        binding.viewpager.adapter = ViewPagerAdapter(this)


    }
}