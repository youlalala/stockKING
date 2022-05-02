package org.techtown.stockking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
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
                    method = "kakao",
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

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.googleLoginBtn.setOnClickListener {
            val intent = mGoogleSignInClient.signInIntent

            MySharedPreferences.setMethod(this,"google")
            startActivityForResult(intent, 1)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            try {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                Log.i("SSS","account"+account.toString())
                MySharedPreferences.setToken(this, account.idToken!!)
                MySharedPreferences.setMethod(this, "google")
                val userInfo = UserModel(
                    method = "google",
                    token= MySharedPreferences.getToken(this)
                )

                Log.i("sss","userInfo : "+userInfo)

                ApiWrapper.postToken(userInfo){
                }
                Log.i("SSS","구글 로그인 성공")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }catch(e: ApiException){
                Log.i("SSS","구글 로그인 실패:"+e)
            }

        }
    }


}

