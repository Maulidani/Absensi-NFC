package domain.skripsi.absensinfc.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.hdodenhof.circleimageview.CircleImageView
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.utils.Constant
import domain.skripsi.absensinfc.utils.PreferencesHelper

class ProfileFragment : Fragment() {
    private lateinit var sharedPref: PreferencesHelper
    private lateinit var tvLogout: TextView
    private lateinit var nameTextField: TextInputLayout
    private lateinit var nipTextField: TextInputLayout
    private lateinit var phoneTextField: TextInputLayout
    private lateinit var emailTextField: TextInputLayout
    private lateinit var imgProfile: CircleImageView

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
        nameTextField = requireView().findViewById(R.id.nameTextField)
        nipTextField = requireView().findViewById(R.id.nipTextField)
        phoneTextField = requireView().findViewById(R.id.phoneTextField)
        emailTextField = requireView().findViewById(R.id.emailTextField)
        imgProfile = requireView().findViewById(R.id.imgProfile)

        nameTextField.hint = sharedPref.getString(PreferencesHelper.PREF_USER_NAME)
        nipTextField.hint = sharedPref.getString(PreferencesHelper.PREF_USER_NIP)
        phoneTextField.hint = sharedPref.getString(PreferencesHelper.PREF_USER_PHONE)
        emailTextField.hint = sharedPref.getString(PreferencesHelper.PREF_USER_EMAIL)

        imgProfile.load(
            Constant.BASE_URL  +
                    sharedPref.getString(PreferencesHelper.PREF_USER_PHOTO)
        ) {
            crossfade(true)
            placeholder(R.drawable.logo_unm)
            transformations(CircleCropTransformation())
        }

        tvLogout.setOnClickListener {
            sharedPref.logout()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            activity?.finish()
        }

    }

}