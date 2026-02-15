package pe.com.cibertec.inventarioferreteriazamora.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import pe.com.cibertec.inventarioferreteriazamora.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnNuevo = findViewById<MaterialButton>(R.id.btnNuevoProducto)
        val btnVer = findViewById<MaterialButton>(R.id.btnVerProductos)
        val btnSync = findViewById<MaterialButton>(R.id.btnSincronizar)

        btnNuevo.setOnClickListener {
            val intent = Intent(this, NuevoProductoActivity::class.java)
            startActivity(intent)
        }

        btnVer.setOnClickListener {
            val intent = Intent(this, ListaProductoActivity::class.java)
            startActivity(intent)
        }

        btnSync.setOnClickListener {
            Toast.makeText(this, "Sincronizacion pendiente de configurar", Toast.LENGTH_SHORT).show()
        }
    }
}
