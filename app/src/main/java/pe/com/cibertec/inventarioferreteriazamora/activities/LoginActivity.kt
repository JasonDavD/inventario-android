package pe.com.cibertec.inventarioferreteriazamora.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerUsuario

class LoginActivity : AppCompatActivity() {

    private lateinit var txtUsuario: TextInputEditText
    private lateinit var txtContrasena: TextInputEditText
    private lateinit var btnIngresar: MaterialButton
    private val controllerUsuario = ControllerUsuario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("sesion", MODE_PRIVATE)
        if (prefs.getBoolean("logueado", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        txtUsuario = findViewById(R.id.txtUsuario)
        txtContrasena = findViewById(R.id.txtContrasena)
        btnIngresar = findViewById(R.id.btnIngresar)

        btnIngresar.setOnClickListener {
            iniciarSesion()
        }
    }

    private fun iniciarSesion() {
        val usuario = txtUsuario.text.toString().trim()
        val contrasena = txtContrasena.text.toString().trim()

        if (usuario.isEmpty()) {
            txtUsuario.error = "Ingresa tu usuario"
            return
        }
        if (contrasena.isEmpty()) {
            txtContrasena.error = "Ingresa tu contraseña"
            return
        }

        val usuarioEncontrado = controllerUsuario.buscarPorCredenciales(usuario, contrasena)

        if (usuarioEncontrado == null) {
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Usuario o contraseña incorrectos")
                .setPositiveButton("Aceptar", null)
                .show()
            return
        }

        val editor = getSharedPreferences("sesion", MODE_PRIVATE).edit()
        editor.putBoolean("logueado", true)
        editor.putString("rol", usuarioEncontrado.rol)
        editor.putString("nombre", usuarioEncontrado.nombre)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
