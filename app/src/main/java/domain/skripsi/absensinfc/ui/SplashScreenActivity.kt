package domain.skripsi.absensinfc.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.model.ResponseData
import domain.skripsi.absensinfc.model.ResponseModel
import domain.skripsi.absensinfc.network.ApiClient
import domain.skripsi.absensinfc.utils.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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


            } else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun getClass() {

        ApiClient.SetContext(applicationContext).instancesWithToken.apiJadwalDosen()
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    val responseBody = response.body()
                    val status = responseBody?.status
                    val message = responseBody?.message
                    val data = responseBody?.data

                    if (response.isSuccessful && status == true) {
                        Log.e(this@SplashScreenActivity.toString(), "onResponse: $response")

                        if (data != null) {

                           intent(data)

                        } else {
                            Toast.makeText(applicationContext, "Tidak ada data", Toast.LENGTH_SHORT).show()

                        }

                    } else {
                        Log.e(this@SplashScreenActivity.toString(), "onResponse: $response")
                        Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

                    Log.e(this@SplashScreenActivity.toString(), "onFailure: $t")
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()

                }

            })
    }

    private fun intent(data: ArrayList<ResponseData>) {

        startActivity(Intent(applicationContext, HomeActivity::class.java))
        finish()

        Log.e(this@SplashScreenActivity.toString(), "sharedPref: PREF_USER_TOKEN : ${sharedPref.getString(
            PreferencesHelper.PREF_USER_TOKEN
        )}")

    }
}