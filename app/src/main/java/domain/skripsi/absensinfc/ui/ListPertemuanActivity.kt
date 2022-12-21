package domain.skripsi.absensinfc.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.adapter.ClassAdapter
import domain.skripsi.absensinfc.model.ResponseModel
import domain.skripsi.absensinfc.network.ApiClient
import domain.skripsi.absensinfc.utils.Constant.URL_REPORT_DOWNLOAD
import domain.skripsi.absensinfc.utils.PreferencesHelper
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.time.LocalDateTime


class ListPertemuanActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    private val btnDownloadReport: MaterialButton by lazy { findViewById(R.id.btnDownloadReport) }
    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val tvHead: TextView by lazy { findViewById(R.id.tvHead) }
    private val loading: ProgressBar by lazy { findViewById(R.id.progressBar) }
    private val rvPertemuan: RecyclerView by lazy { findViewById(R.id.rvPertemuan) }

    private var intentKelas: String? = null
    private var intentKelasId: String? = null

    private var pembagianJadwalId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pertemuan)

        sharedPref = PreferencesHelper(applicationContext)
        intentKelas = intent.getStringExtra("kelas").toString()
        intentKelasId = intent.getStringExtra("kelas_id").toString()

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
//                    "https://research.nhm.org/pdfs/10840/10840-001.pdf" //12mb
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


        getPertemuan(intentKelasId!!)

    }

    private fun getPertemuan(intentKelasId: String) {
        loading.visibility = View.VISIBLE

        ApiClient.SetContext(applicationContext).instancesWithToken.apiShowPertemuan(intentKelasId)
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
//            val downloadFile = DownloadFile()
//            downloadFile.startDownload(
//                this,
//                fileName,
//                url,
//                sharedPref.getString(PREF_USER_TOKEN)
//            )
            Toast.makeText(applicationContext, "Mendownload file...", Toast.LENGTH_LONG).show()

            downloadFileRetrofit(url, fileName)
        }
    }

    private fun downloadFileRetrofit(url: String, fileName: String) {
        ApiClient.SetContext(applicationContext).instancesWithToken.downloadFileWithDynamicUrlSync(
            url
        )
            ?.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Download file retrofit", "server contacted and has file")
                        val writtenToDisk: Boolean =
                            writeResponseBodyToDisk(response.body(), fileName)

                        Log.d(
                            "Download file retrofit",
                            "file download was a success? $writtenToDisk"
                        )
                    } else {
                        Log.d("Download file retrofit", "server contact failed")

                        Toast.makeText(
                            applicationContext,
                            "Gagal : request",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Log.e("Download file retrofit", "error")
                    Toast.makeText(
                        applicationContext,
                        "Gagal : ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    private fun writeResponseBodyToDisk(body: ResponseBody?, fileName: String): Boolean {
        return try {
            val filePath =
                downloadPath().toString() + "/" + fileName + ".pdf"
//                File(getExternalFilesDir(null).toString() + File.separator.toString() + "Future Studio Icon.pdf")

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            Log.d(
                "writeResponseBodyToDisk",
                "file : $filePath"
            )
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body?.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body?.byteStream()
                outputStream = FileOutputStream(filePath)
                while (true) {
                    val read: Int? = inputStream?.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read!!)
                    fileSizeDownloaded += read.toLong()
                    Log.d(
                        "writeResponseBodyToDisk",
                        "file download: $fileSizeDownloaded of $fileSize"
                    )
                }
                outputStream.flush()

                Toast.makeText(
                    applicationContext,
                    "Selesai : periksa folder download anda",
                    Toast.LENGTH_SHORT
                ).show()

                true
            } catch (e: IOException) {
                Toast.makeText(
                    applicationContext,
                    "Gagal mendownload : ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            Toast.makeText(
                applicationContext,
                "Gagal mendownload : ${e.message}",
                Toast.LENGTH_SHORT
            ).show()

            false
        }
    }

    private fun downloadPath(): File? {
        var dir: File? = null
        dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString()
            )
        } else {
            File(Environment.getExternalStorageDirectory().toString())
        }

        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            val success = dir.mkdirs()
            if (!success) {
                dir = null
            }
        }
        return dir
    }
}

