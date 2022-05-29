package org.techtown.stockking.module.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import org.techtown.stockking.MainActivity
import org.techtown.stockking.R
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.ActivityLoginBinding
import org.techtown.stockking.model.FirstLoginModel
import org.techtown.stockking.network.ApiWrapperLogin


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val TAG : String = "login"

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                //Login Fail
                Log.d("sss", "로그인 실패", error)
            } else if (token != null) {
                //Login Success
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    }
                    else if (user != null) {
                        Log.i(TAG, "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

                        //first login request
                        val userInfo = FirstLoginModel(
                            accessToken= token.accessToken
                        )

                        ApiWrapperLogin.postFirstLogin("kakao",userInfo){
                            if (it != null) {
                                MySharedPreferences.setToken(this, it.result.userToken)
                                MySharedPreferences.setUserName(this, it.result.name)
                                MySharedPreferences.setMethod(this, "kakao")
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            }
        }

        //kakao Login button
        binding.kakaoLoginBtn.setOnClickListener {
            UserApiClient.instance.run {
                if (isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    //카카오톡이 있을 경우
                    loginWithKakaoTalk(this@LoginActivity, callback = callback)
                } else {
                    //카카오톡이 없을 경우 카카오 계정으로 로그인하기
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


            startActivityForResult(intent, 1)
        }

    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            // TODO(developer): send ID Token to server and validate
            val userInfo = FirstLoginModel(
                accessToken= account.idToken!!
            )

            ApiWrapperLogin.postFirstLogin("google",userInfo){
                if (it != null) {
                    MySharedPreferences.setToken(this, it.result.userToken)
                    MySharedPreferences.setUserName(this, it.result.name)
                    MySharedPreferences.setMethod(this, "google")
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            //updateUI(account)
        } catch (e: ApiException) {
            Log.i("SSS", "handleSignInResult:error", e)
            //updateUI(null)
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
                handleSignInResult(task);
//                val account = task.getResult(ApiException::class.java)
//                Log.i("SSS","account token\n"+account.idToken.toString())
//                //first login request
//                val userInfo = FirstLoginModel(
//                    accessToken= account.idToken!!
//                )
//
//                ApiWrapperLogin.postFirstLogin("google",userInfo){
//                    if (it != null) {
//                        MySharedPreferences.setToken(this, account.idToken!!)
//                        MySharedPreferences.setMethod(this, "google")
//                    }
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
            }catch(e: ApiException){
                Log.i("SSS","구글 로그인 실패:"+e)
            }

        }
    }


}

