package domain.skripsi.absensinfc.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.adapter.ClassAdapter
import domain.skripsi.absensinfc.model.ResponseModel
import domain.skripsi.absensinfc.model.ResponseModelDataIsObject
import domain.skripsi.absensinfc.network.ApiClient
import domain.skripsi.absensinfc.utils.Constant.setShowProgress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClassActivity : AppCompatActivity() {
    private val rvTodayClass: RecyclerView by lazy { findViewById(R.id.rvTodayClass) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class)

        getClass()
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
                        Log.e(this@ClassActivity.toString(), "onResponse: $response")

                        if (data != null) {

                            val adapter = data.let { ClassAdapter( it, "today") }
                            rvTodayClass.layoutManager = LinearLayoutManager(applicationContext)
                            rvTodayClass.adapter = adapter

                        } else {
                            Toast.makeText(applicationContext, "Tidak ada data", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Log.e(this@ClassActivity.toString(), "onResponse: $response")
                        Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

                    Log.e(this@ClassActivity.toString(), "onFailure: $t")
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()

                }

            })
    }
}