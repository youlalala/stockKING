package org.techtown.stockking.module.common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.ActivityLoginBinding
import org.techtown.stockking.model.UserModel
import org.techtown.stockking.network.ApiWrapper

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                //Login Fail
                Log.d("sss", "로그인 실패", error)
            } else if (token != null) {
                //Login Success

                MySharedPreferences.setToken(this, token.accessToken)
                MySharedPreferences.setMethod(this, "kakao")

                val userInfo = UserModel(
                    token= MySharedPreferences.getToken(this)
                )

                Log.i("sss","userInfo : "+userInfo)

                ApiWrapper.postToken(userInfo){
                    Log.i("sss","it!!!"+it.toString())
                }

                Log.i("SSS","로그인성공")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }


        //kakao Login button
        binding.kakaoLoginBtn.setOnClickListener {
            UserApiClient.instance.run {
                if (isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    //카카오톡이 있을 경우
                    loginWithKakaoTalk(this@LoginActivity, callback = callback)
                } else {
                    //카카오ㅗㄱ이 없을 경우 카카오 계정으로 로그인하기
                    loginWithKakaoAccount(this@LoginActivity, callback = callback)
                }
            }
        }

    }


}

