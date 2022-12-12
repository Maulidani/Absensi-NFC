package domain.skripsi.absensinfc.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.utils.PreferencesHelper

class ClassStudentActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val tvHead: TextView by lazy { findViewById(R.id.tvHead) }
    private val tvSubHead: TextView by lazy { findViewById(R.id.tvSubHead) }
    private val loading: ProgressBar by lazy { findViewById(R.id.progressBar) }

    private var intentJadwalId: String? = null
    private var intentMatkulName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_student)

        sharedPref = PreferencesHelper(applicationContext)
        intentJadwalId = intent.getStringExtra("jadwal_id").toString()
        intentMatkulName = intent.getStringExtra("matkul_name").toString()

        loading.visibility = View.GONE

        tvHead.text = intentMatkulName
        tvSubHead.text = "Pertemuan "+"0"

        imgBack.setOnClickListener { finish() }

    }
}