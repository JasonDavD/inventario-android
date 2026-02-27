package pe.com.cibertec.inventarioferreteriazamora.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import pe.com.cibertec.inventarioferreteriazamora.R
import kotlin.jvm.java

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.imgLogo)
        val anim = AnimationUtils.loadAnimation(this, R.anim.logo_anim)
        logo.startAnimation(anim)

        Handler(Looper.getMainLooper()).postDelayed({

            val prefs = getSharedPreferences("sesion", MODE_PRIVATE)

            if (prefs.getBoolean("logueado", false)) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }

            finish()

        }, 3000)
    }
}