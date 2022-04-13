package org.techtown.stockking.module.account_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.user.UserApiClient
import org.techtown.stockking.R
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.ActivitySettingBinding
import org.techtown.stockking.module.common.LoginActivity

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{
            finish()
        }
        binding.logoutBtn.setOnClickListener {
            if (MySharedPreferences.getMethod(this) == "kakao") {
                kakaoLogout()
            }
        }
    }
    private fun kakaoLogout(){
        MySharedPreferences.clearPreference(this)
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("SSS", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
                Log.i("SSS", "로그아웃 성공. SDK에서 토큰 삭제됨")
                val intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}