package domain.skripsi.absensinfc.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.model.ResponseModelDataIsObject
import domain.skripsi.absensinfc.network.ApiClient
import domain.skripsi.absensinfc.utils.Constant.setShowProgress
import domain.skripsi.absensinfc.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPasswordActivity : AppCompatActivity() {

    private val btnEdit: MaterialButton by lazy { findViewById(R.id.btnEdit) }
    private val inputOld: TextInputEditText by lazy { findViewById(R.id.inputPasswordOld) }
    private val inputNew: TextInputEditText by lazy { findViewById(R.id.inputPasswordNew) }
    private val inputConfirm: TextInputEditText by lazy { findViewById(R.id.inputConfirmPassword) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)

        btnEdit.setOnClickListener {
            val old = inputOld.text.toString()
            val new = inputNew.text.toString()
            val confirm = inputConfirm.text.toString()

            if (old.isNotEmpty() && new.isNotEmpty() && confirm.isNotEmpty()) {

                if (new == confirm) {
                    editPassword(old, new, confirm)
                } else {
                    Toast.makeText(applicationContext, "Password baru tidak cocok", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Lengkapi data", Toast.LENGTH_SHORT).show()
            }

        }

    }
    private fun editPassword(old: String, new: String, confirm: String) {
        btnEdit.setShowProgress(true)

        ApiClient.SetContext(applicationContext).instancesWithToken.apiEditPassword(old,new,confirm)
            .enqueue(object : Callback<ResponseModelDataIsObject> {
                override fun onResponse(
                    call: Call<ResponseModelDataIsObject>,
                    response: Response<ResponseModelDataIsObject>
                ) {
                    val responseBody = response.body()
                    val status = responseBody?.status
                    val message = responseBody?.message

                    if (response.isSuccessful && status == true) {
                        Log.e(this@EditPasswordActivity.toString(), "onResponse: $response")

                        Toast.makeText(applicationContext, "Berhasil", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()
                    }
                    btnEdit.setShowProgress(false)

                }

                override fun onFailure(call: Call<ResponseModelDataIsObject>, t: Throwable) {

                    Toast.makeText(applicationContext, "Gagal : Terjadi kesalahan "+t.message.toString(), Toast.LENGTH_LONG)
                        .show()

                    btnEdit.setShowProgress(false)
                }

            })
    }

}