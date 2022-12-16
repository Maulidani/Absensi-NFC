package domain.skripsi.absensinfc.ui

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.model.ResponseModel
import domain.skripsi.absensinfc.network.ApiClient
import domain.skripsi.absensinfc.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ScanNFCActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    //    private val MIME_TEXT_PLAIN = "text/plain"
    private var nfcAdapter: NfcAdapter? = null

    //    private var intentFiltersArray: Array<IntentFilter>? = null
//    private val techListsArray = arrayOf(arrayOf(NfcF::class.java.name))
    private var pendingIntent: PendingIntent? = null
    private var readIntentFilter: Array<IntentFilter>? = null

    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val loading: ProgressBar by lazy { findViewById(R.id.progressBar) }
    private val tvSeeAll: TextView by lazy { findViewById(R.id.tvSeeAll) }
    private val tvHead: TextView by lazy { findViewById(R.id.tvHead) }
    private val cardResultNfc: CardView by lazy { findViewById(R.id.cardResultNfc) }
    private val tvStudentName: TextView by lazy { findViewById(R.id.tvStudentName) }
    private val tvStudentNim: TextView by lazy { findViewById(R.id.tvStudentNim) }
    private val tvStudentStatus: TextView by lazy { findViewById(R.id.tvStudentStatus) }

    private var intentJadwalId: String? = null
    private var intentMatkulName: String? = null
    private var intentPertemuan: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_nfc)

        sharedPref = PreferencesHelper(applicationContext)

        intentJadwalId = intent.getStringExtra("jadwal_id").toString()
        intentMatkulName = intent.getStringExtra("matkul_name").toString()
        intentPertemuan = intent.getStringExtra("pertemuan").toString()

        loading.visibility = View.GONE

        if (!sharedPref.getBoolean(PreferencesHelper.PREF_IS_LOGIN)) {

            finish()
            Toast.makeText(
                applicationContext,
                "Login terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
        } else if (intentMatkulName == "null") {

            Toast.makeText(
                applicationContext,
                "Pilih matakuliah terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()

        } else {
            initNfc()
        }

        tvHead.text = "Absen : $intentMatkulName"

        imgBack.setOnClickListener { finish() }

        tvSeeAll.setOnClickListener {
            startActivity(
                Intent(applicationContext, ClassStudentActivity::class.java)
                    .putExtra("jadwal_id", intentJadwalId)
                    .putExtra("matkul_name", intentMatkulName)
                    .putExtra("pertemuan", intentPertemuan)
            )
        }

        //
        tvStudentName.text = "-"
        tvStudentNim.text = "-"
        tvStudentStatus.text = "-"

    }

    private fun initNfc() {
        try {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)

            if (nfcAdapter == null) {
                // Stop here, we definitely need NFC
                Toast.makeText(
                    applicationContext,
                    "This device doesn't support NFC.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()

            } else if (!nfcAdapter!!.isEnabled) {
                Toast.makeText(applicationContext, "NFC tidak aktif.", Toast.LENGTH_SHORT).show();
                finish()
            } else {

                try {

                    val objIntent = Intent(this, javaClass)
                    objIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

                    pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        PendingIntent.getActivity(
                            this,
                            0,
                            objIntent,
                            PendingIntent.FLAG_MUTABLE
                        )
                    } else {
                        PendingIntent.getActivity(
                            this,
                            0,
                            objIntent,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    }

                    val javadudeIntentFilter: IntentFilter =
                        IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
                    javadudeIntentFilter.addDataScheme("http")
                    javadudeIntentFilter.addDataAuthority("javadude.com", null)

                    val textIntentFilter: IntentFilter =
                        IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED, "text/plain")
                    readIntentFilter = arrayOf(javadudeIntentFilter, textIntentFilter)

                    processNfc(intent)

                } catch (ex: Exception) {
                    Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
                }

            }

        } catch (ex: Exception) {
            Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun processNfc(intent: Intent) {

        var messages: Array<Parcelable>? = null
        messages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        } else {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        }

        val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)

        if (messages != null && tag != null) {

            val tagId = tag.id?.let { byteArrayToHexString(it) }
            Log.e(
                "processNfc: ",
                "WELL_KNOWN: ID: $tagId"
            )

            for (message: Parcelable in messages) {
                val ndefMessage = message as NdefMessage

                for (record: NdefRecord in ndefMessage.records) {
                    when (record.tnf) {
                        NdefRecord.TNF_WELL_KNOWN -> {
                            if (Arrays.equals(record.type, NdefRecord.RTD_TEXT)) {

                                val textArray: ByteArray = Arrays.copyOfRange(
                                    record.payload,
                                    record.payload[0].toInt() + 1,
                                    record.payload.size
                                )
                                val result = String(textArray)

                                Log.e(
                                    "processNfc: ",
                                    "WELL_KNOWN: TEXT: $result : ${record.payload}"
                                )

//                                tvStudentStatus.text =
//                                    "jadwal id : $intentJadwalId , nim : $result , id : $tagId"

                                attendStudent(result, tagId!!)

                            } else if (Arrays.equals(record.type, NdefRecord.RTD_URI)) {

                                Log.e(
                                    "processNfc: ",
                                    "WELL_KNOWN: URI: ${record.payload}"
                                )

                            }
                        }

                    }
                }

            }

        } else {
            //
        }
    }

    private fun attendStudent(resultNim: String, tagId: String) {
        loading.visibility = View.VISIBLE

        intentJadwalId?.toIntOrNull()?.let {
            ApiClient.SetContext(applicationContext).instancesWithToken.apiAbsen(
                it,
                tagId,
//                resultNim
            )
                .enqueue(object : Callback<ResponseModel> {
                    override fun onResponse(
                        call: Call<ResponseModel>,
                        response: Response<ResponseModel>
                    ) {
                        val responseBody = response.body()
                        val name = responseBody?.nama
                        val nim = responseBody?.nim
                        val status = responseBody?.status
                        val message = responseBody?.message
                        val data = responseBody?.data

                        if (response.isSuccessful && status == true) {
                            Log.e(this@ScanNFCActivity.toString(), "onResponse: $response")

                            if (message == "Mahasiswa sudah absen") {
                                tvStudentName.text = "Nama : -"
                                tvStudentNim.text = "NIM : -"
                                tvStudentStatus.text = "Data ini sudah absen"
                            } else {

                                // init view result
                                tvStudentName.text = "Nama : $name"
                                tvStudentNim.text = "NIM : $nim"
                                tvStudentStatus.text = "Absen berhasil (hadir)"

                            }

                        } else {
                            if (message == "Mahasiswa sudah absen") {
                                tvStudentName.text = "Nama : -"
                                tvStudentNim.text = "NIM : -"
                                tvStudentStatus.text = "Data ini sudah absen"
                            } else {

                                tvStudentName.text = "Nama : -"
                                tvStudentNim.text = "NIM : -"
                                tvStudentStatus.text = "Data ini tidak terdaftar"

                                Toast.makeText(
                                    applicationContext,
                                    "Tidak ada data",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                        loading.visibility = View.GONE

                    }

                    override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

                        Log.e(this@ScanNFCActivity.toString(), "onFailure: $t")
                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                            .show()

                        tvStudentName.text = "Nama : -"
                        tvStudentNim.text = "NIM : -"
                        tvStudentStatus.text = "Data ini tidak terdaftar"

                        loading.visibility = View.GONE
                    }

                })
        }


    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)

        if (newIntent != null) {
            processNfc(newIntent)
        }

    }

    private fun enableRead() {
        NfcAdapter.getDefaultAdapter(this)
            .enableForegroundDispatch(this, pendingIntent, readIntentFilter, null)
    }

    private fun disableRead() {
        NfcAdapter.getDefaultAdapter(this).disableReaderMode(this)
    }

    override fun onResume() {
        super.onResume()
        enableRead()
    }

    override fun onPause() {
        super.onPause()
        disableRead()
    }

    private fun byteArrayToHexString(inarray: ByteArray): String? {
        var i: Int
        var j: Int
        var `in`: Int
        val hex = arrayOf(
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "A",
            "B",
            "C",
            "D",
            "E",
            "F"
        )
        var out = ""
        j = 0
        while (j < inarray.size) {
            `in` = inarray[j].toInt() and 0xff
            i = `in` shr 4 and 0x0f
            out += hex[i]
            i = `in` and 0x0f
            out += hex[i]
            ++j
        }
        return out
    }

}
