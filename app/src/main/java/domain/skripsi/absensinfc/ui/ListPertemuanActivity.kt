package domain.skripsi.absensinfc.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.adapter.ClassAdapter
import domain.skripsi.absensinfc.model.ResponseModel
import domain.skripsi.absensinfc.network.ApiClient
import domain.skripsi.absensinfc.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPertemuanActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val tvHead: TextView by lazy { findViewById(R.id.tvHead) }
    private val loading: ProgressBar by lazy { findViewById(R.id.progressBar) }
    private val rvPertemuan: RecyclerView by lazy { findViewById(R.id.rvPertemuan) }

    private var intentKelas: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pertemuan)

        sharedPref = PreferencesHelper(applicationContext)
        intentKelas = intent.getStringExtra("kelas").toString()

        loading.visibility = View.GONE

        tvHead.text = "Kelas " + intentKelas

        imgBack.setOnClickListener { finish() }

        getPertemuan()

    }

    private fun getPertemuan() {
        loading.visibility = View.VISIBLE

        ApiClient.SetContext(applicationContext).instancesWithToken.apiShowPertemuan()
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
                        Log.e(this@ListPertemuanActivity.toString(), "onResponse: $response")

                        if (data != null) {

                            val adapter = data.let { ClassAdapter(it, "pertemuan") }
                            rvPertemuan.layoutManager =
                                LinearLayoutManager(applicationContext)
                            rvPertemuan.adapter = adapter

                            if (data.size == 0) {
                                Toast.makeText(
                                    applicationContext,
                                    "Tidak ada data",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Tidak ada data",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                    } else {
                        sharedPref.logout()
                        finish()

                        Log.e(this@ListPertemuanActivity.toString(), "onResponse: $response")
                        Toast.makeText(
                            applicationContext,
                            "Gagal : login ulang",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    loading.visibility = View.GONE

                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

                    Log.e(this@ListPertemuanActivity.toString(), "onFailure: $t")
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()

                    loading.visibility = View.GONE

                }

            })
    }
}
