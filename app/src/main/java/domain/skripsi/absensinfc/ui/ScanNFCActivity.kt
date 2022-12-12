package domain.skripsi.absensinfc.ui

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.utils.PreferencesHelper

class ScanNFCActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    private var nfcAdapter: NfcAdapter? = null
    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
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

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        intentJadwalId = intent.getStringExtra("jadwal_id").toString()
        intentMatkulName = intent.getStringExtra("matkul_name").toString()
        intentPertemuan = intent.getStringExtra("pertemuan").toString()

//        cardResultNfc.visibility = View.GONE

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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val tagFromIntent: Tag? = intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        val nfc = NfcA.get(tagFromIntent)

        val atqa: ByteArray = nfc.atqa
        val sak: Short = nfc.sak

        nfc.connect()
        val isConnected = nfc.isConnected

        if (isConnected) {
            val receivedData: ByteArray = nfc.transceive(atqa)
            //code to handle the received data
            // Received data would be in the form of a byte array that can be converted to string
            //NFC_READ_COMMAND would be the custom command you would have to send to your NFC Tag in order to read it

//            cardResultNfc.visibility = View.VISIBLE

        } else {
            Toast.makeText(applicationContext, "NFC not connected", Toast.LENGTH_SHORT).show()
            Log.e("ans", "Not connected")
        }
    }

    private fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()
        filters[0] = IntentFilter()
        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType("text/plain")
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException(ex)
            }
        }
        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    override fun onResume() {
        super.onResume()
        enableForegroundDispatch(this, this.nfcAdapter)

        if (!sharedPref.getBoolean(PreferencesHelper.PREF_IS_LOGIN)) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }
}