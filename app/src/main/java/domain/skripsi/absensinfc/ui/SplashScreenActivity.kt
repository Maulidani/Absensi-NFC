package domain.skripsi.absensinfc.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.network.DownloadFile
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

            delay(1500)

            if (sharedPref.getBoolean(PreferencesHelper.PREF_IS_LOGIN)) {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()

            } else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        }

    }

}
