package domain.skripsi.absensinfc.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import coil.load
import de.hdodenhof.circleimageview.CircleImageView
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.utils.PreferencesHelper

class ProfileFragment : Fragment() {
    private lateinit var sharedPref: PreferencesHelper
    private lateinit var tvLogout: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = PreferencesHelper(requireActivity())

        tvLogout = requireView().findViewById(R.id.tvLogout)

        tvLogout.setOnClickListener {
            sharedPref.logout()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            activity?.finish()
        }
    }

}