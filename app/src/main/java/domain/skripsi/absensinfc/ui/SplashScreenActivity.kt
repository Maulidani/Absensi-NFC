package domain.skripsi.absensinfc.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.utils.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPref = PreferencesHelper(applicationContext)

    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)

            if (sharedPref.getBoolean(PreferencesHelper.PREF_IS_LOGIN)) {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()

                Log.e(this@SplashScreenActivity.toString(), "sharedPref: PREF_USER_TOKEN : ${sharedPref.getString(
                    PreferencesHelper.PREF_USER_TOKEN
                )}")

            } else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        }
    }
}