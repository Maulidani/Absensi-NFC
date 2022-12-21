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
import domain.skripsi.absensinfc.adapter.ClassAdapter
import domain.skripsi.absensinfc.model.ResponseModel
import domain.skripsi.absensinfc.network.ApiClient
import domain.skripsi.absensinfc.network.DownloadFile
import domain.skripsi.absensinfc.utils.Constant
import domain.skripsi.absensinfc.utils.Constant.URL_REPORT_DOWNLOAD
import domain.skripsi.absensinfc.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class ListPertemuanActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    private val btnDownloadReport: MaterialButton by lazy { findViewById(R.id.btnDownloadReport) }
    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val tvHead: TextView by lazy { findViewById(R.id.tvHead) }
    private val loading: ProgressBar by lazy { findViewById(R.id.progressBar) }
    private val rvPertemuan: RecyclerView by lazy { findViewById(R.id.rvPertemuan) }

    private var intentKelas: String? = null

    private var pembagianJadwalId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pertemuan)

        sharedPref = PreferencesHelper(applicationContext)
        intentKelas = intent.getStringExtra("kelas").toString()

        loading.visibility = View.GONE

        tvHead.text = "Kelas " + intentKelas

        imgBack.setOnClickListener { finish() }

        btnDownloadReport.isEnabled = false

        btnDownloadReport.setOnClickListener {
            if (pembagianJadwalId != null) {

                val current = LocalDateTime.now()

                downloadFile(
                    "report-absen-${current.dayOfMonth}-${current.monthValue}-${current.year}-(${
                        (100..999).shuffled().last()
                    })",
                    URL_REPORT_DOWNLOAD + pembagianJadwalId
//                    "http://pii.or.id/uploads/dummies.pdf"
                )

            } else {
                Toast.makeText(
                    applicationContext,
                    "Gagal : Terjadi kesalahan",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }


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
                            } else {
                                pembagianJadwalId = data[0].pembagian_jadwal_id.toString()
                                btnDownloadReport.isEnabled = true

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


    private fun downloadFile(fileName: String, url: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(
                    applicationContext,
                    "Izinkan penyimpanan terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1000
                );

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            val downloadFile = DownloadFile()
            downloadFile.startDownload(this, fileName, url)
        }
    }
}
