package domain.skripsi.absensinfc.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import domain.skripsi.absensinfc.R

class LoginActivity : AppCompatActivity() {
    private val btnLogin: MaterialButton by lazy { findViewById(R.id.btnLogin) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }

    }
}