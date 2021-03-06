package org.techtown.stockking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.kakao.sdk.user.UserApiClient
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.ActivitySettingBinding
import org.techtown.stockking.module.login.LoginActivity
import org.techtown.stockking.network.ApiWrapperLogin

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{
            finish()
        }
        binding.withdrawBtn.setOnClickListener {
            ApiWrapperLogin.deleteAccount(MySharedPreferences.getToken(this)){
                if(it?.code==200){
                    val intent= Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }
        binding.logoutBtn.setOnClickListener {
            if (MySharedPreferences.getMethod(this) == "kakao") {
                kakaoLogout()
            }else if(MySharedPreferences.getMethod(this) == "google"){
                googleLogout()
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
                val intent= Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
    private fun googleLogout(){
        //preference clear
        MySharedPreferences.clearPreference(this)
        // 구글 로그아웃을 위해 로그인 세션 가져오기
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInClient.signOut()
            .addOnCompleteListener(this, OnCompleteListener<Void?> {
                val intent= Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            })
    }
}