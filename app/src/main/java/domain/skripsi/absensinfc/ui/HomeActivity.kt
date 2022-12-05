package domain.skripsi.absensinfc.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.utils.PreferencesHelper

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    private val bottomNavigation: BottomNavigationView by lazy { findViewById(R.id.bottomNavigation) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPref = PreferencesHelper(applicationContext)

        loadFragment(HomeFragment())
        bottomNavigation.setOnItemSelectedListener { id ->
            when (id.itemId) {
                R.id.navHome -> loadFragment(HomeFragment())
                R.id.navClass -> loadFragment(AllClassFragment())
                R.id.navProfile -> loadFragment(ProfileFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame, fragment)
            commit()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!sharedPref.getBoolean(PreferencesHelper.PREF_IS_LOGIN)) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }
}