package domain.skripsi.absensinfc.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.model.ResponseData
import domain.skripsi.absensinfc.model.ResponseModelDataIsObject
import domain.skripsi.absensinfc.network.ApiClient
import domain.skripsi.absensinfc.utils.Constant.setShowProgress
import domain.skripsi.absensinfc.utils.PreferencesHelper
import domain.skripsi.absensinfc.utils.PreferencesHelper.Companion.PREF_IS_LOGIN
import domain.skripsi.absensinfc.utils.PreferencesHelper.Companion.PREF_USER_EMAIL
import domain.skripsi.absensinfc.utils.PreferencesHelper.Companion.PREF_USER_NAME
import domain.skripsi.absensinfc.utils.PreferencesHelper.Companion.PREF_USER_NIP
import domain.skripsi.absensinfc.utils.PreferencesHelper.Companion.PREF_USER_PHONE
import domain.skripsi.absensinfc.utils.PreferencesHelper.Companion.PREF_USER_PHOTO
import domain.skripsi.absensinfc.utils.PreferencesHelper.Companion.PREF_USER_TOKEN
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    private val btnLogin: MaterialButton by lazy { findViewById(R.id.btnLogin) }
    private val inputNip: TextInputEditText by lazy { findViewById(R.id.inputNip) }
    private val inputPassword: TextInputEditText by lazy { findViewById(R.id.inputPassword) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPref = PreferencesHelper(applicationContext)

        btnLogin.setOnClickListener {
            val nip = inputNip.text.toString()
            val password = inputPassword.text.toString()

            if (nip.isNotEmpty() && password.isNotEmpty()) {
                login(nip, password)
            } else {
                Toast.makeText(applicationContext, "Lengkapi data", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onResume() {
        super.onResume()

        if (sharedPref.getBoolean(PREF_IS_LOGIN)) {
            finish()

        } else {
            //
        }
    }

    private fun login(nip: String, password: String) {
        btnLogin.setShowProgress(true)

        ApiClient.instancesNoToken.apiLogin(nip, password)
            .enqueue(object : Callback<ResponseModelDataIsObject> {
                override fun onResponse(
                    call: Call<ResponseModelDataIsObject>,
                    response: Response<ResponseModelDataIsObject>
                ) {
                    val responseBody = response.body()
                    val status = responseBody?.status
                    val message = responseBody?.message
                    val token = responseBody?.token
                    val data = responseBody?.data

                    if (response.isSuccessful && status == true) {
                        Log.e(this@LoginActivity.toString(), "onResponse: $response")
                        if (token != null) {
                            saveSession(token, data)
                            Log.e(this@LoginActivity.toString(), "onResponse: TOKEN : $token")

                        } else {
                            Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Log.e(this@LoginActivity.toString(), "onResponse: $response")
                        Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()
                    }
                    btnLogin.setShowProgress(false)

                }

                override fun onFailure(call: Call<ResponseModelDataIsObject>, t: Throwable) {

                    Log.e(this@LoginActivity.toString(), "onFailure: $t")
                    Toast.makeText(applicationContext, "Gagal : Terjadi kesalahan "+t.message.toString(), Toast.LENGTH_LONG)
                        .show()

                    btnLogin.setShowProgress(false)
                }

            })
    }

    private fun saveSession(token: String, data: ResponseData?) {

        sharedPref.logout()

        if (data != null) {
            sharedPref.put(PREF_USER_TOKEN, token)
            sharedPref.put(PREF_IS_LOGIN, true)

            sharedPref.put(PREF_USER_NAME, data.user[0].nama)
            sharedPref.put(PREF_USER_NIP, data.user[0].nip.toString())
            sharedPref.put(PREF_USER_PHONE, data.user[0].no_tlp)
            sharedPref.put(PREF_USER_EMAIL, data.user[0].email)
            sharedPref.put(PREF_USER_PHOTO, data.user[0].foto)

            Log.e(
                this@LoginActivity.toString(),
                "sharedPref: PREF_USER_TOKEN : ${sharedPref.getString(PREF_USER_TOKEN)}"
            )
            Log.e(
                this@LoginActivity.toString(),
                "sharedPref: PREF_IS_LOGIN : ${sharedPref.getBoolean(PREF_IS_LOGIN)}"
            )

            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()

        } else {
            Toast.makeText(applicationContext, "Gagal : Tidak ada data user", Toast.LENGTH_SHORT)
                .show()
        }

    }
}