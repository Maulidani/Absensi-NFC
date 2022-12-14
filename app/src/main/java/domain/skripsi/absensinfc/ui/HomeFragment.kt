package domain.skripsi.absensinfc.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.button.MaterialButton
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.model.ResponseData
import domain.skripsi.absensinfc.model.ResponseModel
import domain.skripsi.absensinfc.network.ApiClient
import domain.skripsi.absensinfc.utils.PreferencesHelper
import domain.skripsi.absensinfc.utils.PreferencesHelper.Companion.PREF_USER_NAME
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors
import java.util.stream.LongStream

class HomeFragment : Fragment() {
    private lateinit var sharedPref: PreferencesHelper

    lateinit var pieChart: PieChart
    lateinit var loading: ProgressBar
    lateinit var btnPresence: MaterialButton
    lateinit var tvTodayClass: TextView
    lateinit var tvProfileName: TextView

    lateinit var tvClassToday: TextView
    lateinit var tvTimeClass: TextView
    lateinit var tvRoomClass: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = PreferencesHelper(requireActivity())

        loading = requireView().findViewById(R.id.progressBar)
        tvProfileName = requireView().findViewById(R.id.tvProfileName)
        btnPresence = requireView().findViewById(R.id.btnPresence)
        tvTodayClass = requireView().findViewById(R.id.tvTodayClass)
        tvClassToday = requireView().findViewById(R.id.tvClassToday)
        tvTimeClass = requireView().findViewById(R.id.tvTimeClass)
        tvRoomClass = requireView().findViewById(R.id.tvRoomClass)

        loading.visibility = View.GONE
        btnPresence.isEnabled = false

        tvProfileName.text = sharedPref.getString(PREF_USER_NAME)

//        btnPresence.setOnClickListener // init later after getData

        tvTodayClass.setOnClickListener {
            startActivity(Intent(requireContext().applicationContext, ClassActivity::class.java))
        }

        getClass()
    }

    private fun getClass() {
        loading.visibility = View.VISIBLE

        ApiClient.SetContext(requireActivity()).instancesWithToken.apiJadwalDosen()
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    val responseBody = response.body()
                    val status = responseBody?.status
                    val message = responseBody?.message
                    val data = responseBody?.data

                    if (isAdded && activity != null && view != null) {
                        if (response.isSuccessful && status == true) {
                            Log.e(requireView().toString(), "onResponse: $response")

                            if (data != null) {
                                Log.e(requireView().toString(), "onResponse: $data")
                                initMatkul(data)

                                if (data.size == 0) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Tidak ada jadwal hari ini",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Tidak ada data",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }

                        } else {

                            sharedPref.logout()
                            startActivity(Intent(requireActivity(), LoginActivity::class.java))
                            activity?.finish()

                            Log.e(requireView().toString(), "onResponse: $response")
                            Toast.makeText(
                                requireContext(),
                                "Gagal : login ulang",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        loading.visibility = View.GONE

                    }

                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

                    if (isAdded && activity != null && view != null) {
                        Log.e(requireView().toString(), "onFailure: $t")
                        Toast.makeText(
                            requireContext(),
                            "Gagal : Terjadi kesalahan : " + t.message.toString(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        loading.visibility = View.GONE

                    }

                }

            })
    }

    private fun initMatkul(data: ArrayList<ResponseData>) {

        val formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss")
        val current = LocalDateTime.now()

        val time = current.format(formatterTime)

        var day = current.dayOfWeek.name

        when (day) {
            "MONDAY" -> day = "Senin"
            "TUESDAY" -> day = "Selasa"
            "WEDNESDAY" -> day = "Rabu"
            "THURSDAY" -> day = "Kamis"
            "FRIDAY" -> day = "Jumat"
            "SATURDAY" -> day = "Sabtu"
            else -> {
                Toast.makeText(requireContext(), "Tidak ada jadwal hari ini", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        Log.e(requireView().toString(), "iniMatkul: hari ini: $day")
        Log.e(requireView().toString(), "iniMatkul: hari ini: jam: $time")

        for (i in data) {
            Log.e(
                requireView().toString(),
                "iniMatkul: data matkul: nama: " + i.matkul.nama_matkul
            )
            Log.e(
                requireView().toString(),
                "iniMatkul: data matkul: hari: " + i.hari
            )
            Log.e(
                requireView().toString(),
                "iniMatkul: data matkul: jam mulai: " + i.jam_mulai
            )
            Log.e(
                requireView().toString(),
                "iniMatkul: data matkul: jam selesai: " + i.jam_selesai
            )

            val timeToInt = time.replace(":", "").toInt()
            val timeMatkuMulailToInt = i.jam_mulai.replace(":", "").toInt()
            val timeMatkuSelesailToInt = i.jam_selesai.replace(":", "").toInt()

            if (timeToInt in timeMatkuMulailToInt..timeMatkuSelesailToInt && day == i.hari) {
                btnPresence.isEnabled = true
                tvClassToday.text =
                    "Mata kuliah saat ini : ${i.matkul.nama_matkul}"
                tvTimeClass.text =
                    "Jadwal : ${i.jam_mulai} - ${i.jam_selesai}"
                tvRoomClass.text =
                    "Kode/Kelas : ${i.kelas.kode_kelas}/${i.kelas.kelas}"

                btnPresence.setOnClickListener {
                    if (btnPresence.isEnabled) {
                        startActivity(
                            Intent(requireContext().applicationContext, ScanNFCActivity::class.java)
                                .putExtra("jadwal_id", i.id.toString())
                                .putExtra("matkul_name", i.matkul.nama_matkul)
                                .putExtra("pertemuan", i.pertemuan)
                        )
                    }
                }

                continue
            }
        }

    }

//    private fun initChart() {
//
//        // on below line we are initializing our
//        // variable with their ids.
//        pieChart = requireView().findViewById(R.id.pieChart)
//
//        // on below line we are setting user percent value,
//        // setting description as enabled and offset for pie chart
//        pieChart.setUsePercentValues(true)
//        pieChart.description.isEnabled = false
//        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
//
//        // on below line we are setting drag for our pie chart
//        pieChart.dragDecelerationFrictionCoef = 0.95f
//
//        // on below line we are setting hole
//        // and hole color for pie chart
//        pieChart.isDrawHoleEnabled = true
//        pieChart.setHoleColor(Color.WHITE)
//
//        // on below line we are setting circle color and alpha
//        pieChart.setTransparentCircleColor(Color.WHITE)
//        pieChart.setTransparentCircleAlpha(110)
//
//        // on  below line we are setting hole radius
//        pieChart.holeRadius = 58f
//        pieChart.transparentCircleRadius = 61f
//
//        // on below line we are setting center text
//        pieChart.setDrawCenterText(true)
//
//        // on below line we are setting
//        // rotation for our pie chart
//        pieChart.rotationAngle = 0f
//
//        // enable rotation of the pieChart by touch
//        pieChart.isRotationEnabled = true
//        pieChart.isHighlightPerTapEnabled = true
//
//        // on below line we are setting animation for our pie chart
//        pieChart.animateY(1400, Easing.EaseInOutQuad)
//
//        // on below line we are disabling our legend for pie chart
//        pieChart.legend.isEnabled = false
//        pieChart.setEntryLabelColor(Color.WHITE)
//        pieChart.setEntryLabelTextSize(12f)
//
//        // on below line we are creating array list and
//        // adding data to it to display in pie chart
//        val entries: ArrayList<PieEntry> = ArrayList()
//        entries.add(PieEntry(80f))
//        entries.add(PieEntry(20f))
//
//        // on below line we are setting pie data set
//        val dataSet = PieDataSet(entries, "Mobile OS")
//
//        // on below line we are setting icons.
//        dataSet.setDrawIcons(false)
//
//        // on below line we are setting slice for pie
//        dataSet.sliceSpace = 3f
//        dataSet.iconsOffset = MPPointF(0f, 40f)
//        dataSet.selectionShift = 5f
//
//        // add a lot of colors to list
//        val colors: ArrayList<Int> = ArrayList()
//        colors.add(ResourcesCompat.getColor(resources, R.color.third, null))
//        colors.add(ResourcesCompat.getColor(resources, R.color.black, null))
//
//        // on below line we are setting colors.
//        dataSet.colors = colors
//
//        // on below line we are setting pie data set
//        val data = PieData(dataSet)
//        data.setValueFormatter(PercentFormatter())
//        data.setValueTextSize(15f)
//        data.setValueTypeface(Typeface.DEFAULT_BOLD)
//        data.setValueTextColor(Color.WHITE)
//        pieChart.data = data
//
//        // undo all highlights
//        pieChart.highlightValues(null)
//
//        // loading chart
//        pieChart.invalidate()
//    }
}