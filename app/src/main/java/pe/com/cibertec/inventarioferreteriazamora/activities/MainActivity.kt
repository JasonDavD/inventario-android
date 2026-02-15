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
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerProducto
import pe.com.cibertec.inventarioferreteriazamora.modelos.Producto
import pe.com.cibertec.inventarioferreteriazamora.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val controller = ControllerProducto()

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
            sincronizar()
        }
    }

    private fun sincronizar() {
        val pendientes = controller.listarPendientes()

        if (pendientes.isEmpty()) {
            Toast.makeText(this, "No hay productos pendientes de sincronizar", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Sincronizando ${pendientes.size} producto(s)...", Toast.LENGTH_SHORT).show()

        val api = ApiUtils.getAPIProducto()
        var sincronizados = 0
        var errores = 0

        for (producto in pendientes) {
            val call = api.crearProducto(producto)
            call.enqueue(object : Callback<Producto> {
                override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                    if (response.isSuccessful) {
                        controller.marcarSincronizado(producto.cod)
                        sincronizados++
                    } else {
                        errores++
                    }
                    verificarFin(pendientes.size, sincronizados, errores)
                }

                override fun onFailure(call: Call<Producto>, t: Throwable) {
                    errores++
                    verificarFin(pendientes.size, sincronizados, errores)
                }
            })
        }
    }

    private fun verificarFin(total: Int, sincronizados: Int, errores: Int) {
        if (sincronizados + errores == total) {
            runOnUiThread {
                if (errores == 0) {
                    Toast.makeText(this, "Sincronizacion exitosa: $sincronizados producto(s)", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Sincronizados: $sincronizados, Errores: $errores", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
