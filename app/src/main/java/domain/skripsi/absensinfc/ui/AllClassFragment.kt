package domain.skripsi.absensinfc.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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

class AllClassFragment : Fragment() {
    private lateinit var sharedPref: PreferencesHelper
    private lateinit var rvMatkulAllDosen: RecyclerView
    private lateinit var loading: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_class, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = PreferencesHelper(requireActivity())
        rvMatkulAllDosen = requireView().findViewById(R.id.rvMatkulAllDosen)
        loading = requireView().findViewById(R.id.progressBar)

        loading.visibility = View.GONE

        getAllClass()
    }

    private fun getAllClass() {
        loading.visibility = View.VISIBLE

        ApiClient.SetContext(requireActivity()).instancesWithToken.apiMatkulAllDosen()
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

                                val adapter = data.let { ClassAdapter(it, "all") }
                                rvMatkulAllDosen.layoutManager =
                                    LinearLayoutManager(requireContext())
                                rvMatkulAllDosen.adapter = adapter

                                if (data.size == 0) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Tidak ada data",
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
                        ).show()

                        loading.visibility = View.GONE

                    }

                }

            })
    }

}