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
import pe.com.cibertec.inventarioferreteriazamora.service.ApiProducto
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
        Toast.makeText(this, "Sincronizando...", Toast.LENGTH_SHORT).show()

        val api = ApiUtils.getAPIProducto()

        // Primero subir cambios locales, luego descargar del API
        subirPendientes(api) {
            descargarProductos(api)
        }
    }

    private fun subirPendientes(api: ApiProducto, onComplete: () -> Unit) {
        val pendientes = controller.listarPendientes()

        if (pendientes.isEmpty()) {
            onComplete()
            return
        }

        var procesados = 0
        var sincronizados = 0
        var errores = 0

        for (producto in pendientes) {
            val call = if (producto.idApi > 0) {
                // Producto ya existe en el API -> usar PUT
                api.actualizarProducto(producto.idApi, producto)
            } else {
                // Producto nuevo -> usar POST
                api.crearProducto(producto)
            }

            call.enqueue(object : Callback<Producto> {
                override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                    if (response.isSuccessful) {
                        val productoApi = response.body()
                        val apiId = productoApi?.idApi ?: 0
                        controller.marcarSincronizado(producto.cod, apiId)
                        sincronizados++
                    } else {
                        errores++
                    }
                    procesados++
                    if (procesados == pendientes.size) {
                        mostrarResultadoSync(sincronizados, errores)
                        onComplete()
                    }
                }

                override fun onFailure(call: Call<Producto>, t: Throwable) {
                    errores++
                    procesados++
                    if (procesados == pendientes.size) {
                        mostrarResultadoSync(sincronizados, errores)
                        onComplete()
                    }
                }
            })
        }
    }

    private fun descargarProductos(api: ApiProducto) {
        api.listarProductos().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    val productosApi = response.body() ?: emptyList()
                    var nuevos = 0
                    var actualizados = 0

                    for (producto in productosApi) {
                        val existente = controller.buscarPorIdApi(producto.idApi)
                        if (existente == null) {
                            controller.insertarDesdeApi(producto)
                            nuevos++
                        } else {
                            controller.actualizarDesdeApi(producto)
                            actualizados++
                        }
                    }

                    runOnUiThread {
                        if (nuevos > 0 || actualizados > 0) {
                            Toast.makeText(this@MainActivity,
                                "Descargados: $nuevos nuevos, $actualizados actualizados",
                                Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@MainActivity,
                                "Todo sincronizado",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity,
                            "Error al descargar productos del servidor",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity,
                        "Error de conexion: ${t.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun mostrarResultadoSync(sincronizados: Int, errores: Int) {
        runOnUiThread {
            if (errores == 0 && sincronizados > 0) {
                Toast.makeText(this, "Subidos: $sincronizados producto(s)", Toast.LENGTH_SHORT).show()
            } else if (errores > 0) {
                Toast.makeText(this, "Subidos: $sincronizados, Errores: $errores", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
