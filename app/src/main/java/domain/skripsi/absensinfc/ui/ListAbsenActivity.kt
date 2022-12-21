package domain.skripsi.absensinfc.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.adapter.StudentAbsenAdapter
import domain.skripsi.absensinfc.model.ResponseData
import domain.skripsi.absensinfc.model.ResponseModel
import domain.skripsi.absensinfc.network.ApiClient
import domain.skripsi.absensinfc.network.DownloadFile
import domain.skripsi.absensinfc.utils.Constant
import domain.skripsi.absensinfc.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class ListAbsenActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val tvHead: TextView by lazy { findViewById(R.id.tvHead) }
    private val loading: ProgressBar by lazy { findViewById(R.id.progressBar) }
    private val rvAbsen: RecyclerView by lazy { findViewById(R.id.rvAbsenMahasiswa) }

    private var intentIdPembagianJadwal: String? = null
    private var intentPertemuan: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_absen)

        sharedPref = PreferencesHelper(applicationContext)
        intentIdPembagianJadwal = intent.getStringExtra("id_pembagian_jadwal").toString()
        intentPertemuan = intent.getStringExtra("pertemuan").toString()

        loading.visibility = View.GONE

        tvHead.text = "Pertemuan " + intentPertemuan

        imgBack.setOnClickListener { finish() }

        getAbsenMahasiswa(intentIdPembagianJadwal!!, intentPertemuan!!)
    }

    private fun getAbsenMahasiswa(intentIdPembagianJadwal: String, intentPertemuan: String) {
        loading.visibility = View.VISIBLE

        intentIdPembagianJadwal.toIntOrNull()?.let {
            ApiClient.SetContext(applicationContext).instancesWithToken.apiAbsenPertemuan(
                it,
                intentPertemuan
            )
                .enqueue(object : Callback<ResponseModel> {
                    override fun onResponse(
                        call: Call<ResponseModel>,
                        response: Response<ResponseModel>
                    ) {
                        val responseBody = response.body()
                        val status = responseBody?.status
                        val message = responseBody?.message
                        val data = responseBody?.data
                        val hadir = responseBody?.hadir
                        val alpa = responseBody?.alpa

                        if (response.isSuccessful && status == true) {
                            Log.e(this@ListAbsenActivity.toString(), "onResponse: $response")

                            if (hadir != null || alpa != null) {

                                val listStudent = ArrayList<ResponseData>()

                                val sortedListHadir: List<ResponseData> =
                                    hadir!!.sortedBy { it3 -> it3.jadwal.mahasiswa.nim }

                                val sortedListNotHadir: List<ResponseData> =
                                    alpa!!.sortedBy { it3 -> it3.mahasiswa.nim }

                                for (i in sortedListNotHadir) {
                                    listStudent.add(i)
                                }

                                for (i in sortedListHadir) {
                                    listStudent.add(i)
                                }

                                val adapter =
                                    StudentAbsenAdapter(
                                        listStudent,
                                        "absen-pertemuan"
                                    )
                                rvAbsen.layoutManager =
                                    LinearLayoutManager(applicationContext)
                                rvAbsen.adapter = adapter

                                if (hadir?.size == 0 && alpa?.size == 0) {
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

                            Log.e(this@ListAbsenActivity.toString(), "onResponse: $response")
                            Toast.makeText(
                                applicationContext,
                                "Gagal : login ulang",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        loading.visibility = View.GONE

                    }

                    override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

                        Log.e(this@ListAbsenActivity.toString(), "onFailure: $t")
                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                            .show()

                        loading.visibility = View.GONE

                    }

                })
        }
    }

}