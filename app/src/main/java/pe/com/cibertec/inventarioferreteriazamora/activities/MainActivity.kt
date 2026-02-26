package pe.com.cibertec.inventarioferreteriazamora.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerCategoria
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerProducto
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerProveedor
import pe.com.cibertec.inventarioferreteriazamora.modelos.Categoria
import pe.com.cibertec.inventarioferreteriazamora.modelos.CategoriaRef
import pe.com.cibertec.inventarioferreteriazamora.modelos.Producto
import pe.com.cibertec.inventarioferreteriazamora.modelos.ProductoRequest
import pe.com.cibertec.inventarioferreteriazamora.modelos.Proveedor
import pe.com.cibertec.inventarioferreteriazamora.modelos.ProveedorRef
import pe.com.cibertec.inventarioferreteriazamora.service.ApiCategoria
import pe.com.cibertec.inventarioferreteriazamora.service.ApiProducto
import pe.com.cibertec.inventarioferreteriazamora.service.ApiProveedor
import pe.com.cibertec.inventarioferreteriazamora.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val controllerProducto = ControllerProducto()
    private val controllerCategoria = ControllerCategoria()
    private val controllerProveedor = ControllerProveedor()
    private lateinit var bd: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("sesion", MODE_PRIVATE)
        if (!prefs.getBoolean("logueado", false)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val rol = prefs.getString("rol", "ALMACENERO") ?: "ALMACENERO"
        val nombre = prefs.getString("nombre", "") ?: ""

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        bd = FirebaseDatabase.getInstance().reference
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvBienvenido = findViewById<TextView>(R.id.tvBienvenido)
        tvBienvenido.text = "Bienvenido, $nombre"

        val btnNuevo = findViewById<MaterialButton>(R.id.btnNuevoProducto)
        val btnVer = findViewById<MaterialButton>(R.id.btnVerProductos)
        val btnCategorias = findViewById<MaterialButton>(R.id.btnGestionarCategorias)
        val btnProveedores = findViewById<MaterialButton>(R.id.btnGestionarProveedores)
        val btnSync = findViewById<MaterialButton>(R.id.btnSincronizar)
        val btnFirebase = findViewById<MaterialButton>(R.id.btnFirebase)
        val btnCerrarSesion = findViewById<MaterialButton>(R.id.btnCerrarSesion)

        val esAdmin = rol == "ADMINISTRADOR"
        btnCategorias.visibility = if (esAdmin) View.VISIBLE else View.GONE
        btnProveedores.visibility = if (esAdmin) View.VISIBLE else View.GONE
        btnFirebase.visibility = if (esAdmin) View.VISIBLE else View.GONE

        btnNuevo.setOnClickListener {
            startActivity(Intent(this, NuevoProductoActivity::class.java))
        }

        btnVer.setOnClickListener {
            startActivity(Intent(this, ListaProductoActivity::class.java))
        }

        btnCategorias.setOnClickListener {
            startActivity(Intent(this, ListaCategoriaActivity::class.java))
        }

        btnProveedores.setOnClickListener {
            startActivity(Intent(this, ListaProveedorActivity::class.java))
        }

        btnSync.setOnClickListener {
            sincronizar()
        }

        btnFirebase.setOnClickListener {
            startActivity(Intent(this, ListaFirebaseActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            val editor = prefs.edit()
            editor.clear()
            editor.apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun sincronizar() {
        Toast.makeText(this, "Sincronizando...", Toast.LENGTH_SHORT).show()

        val apiCat = ApiUtils.getAPICategoria()
        val apiProv = ApiUtils.getAPIProveedor()
        val apiProd = ApiUtils.getAPIProducto()

        // Flujo encadenado: borrar pendientes primero, luego subir y descargar
        borrarPendientesCategoria(apiCat) {
            borrarPendientesProveedor(apiProv) {
                borrarPendientesProducto(apiProd) {
                    subirPendientesCategorias(apiCat) {
                        subirPendientesProveedores(apiProv) {
                            descargarCategorias(apiCat) {
                                descargarProveedores(apiProv) {
                                    subirPendientesProductos(apiProd) {
                                        descargarProductos(apiProd)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun borrarPendientesCategoria(api: ApiCategoria, onComplete: () -> Unit) {
        val pendientes = controllerCategoria.listarPendientesBorrar()

        if (pendientes.isEmpty()) {
            onComplete()
            return
        }

        var procesados = 0

        for (categoria in pendientes) {
            api.eliminarCategoria(categoria.idApi).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        controllerCategoria.eliminar(categoria.cod)
                    }
                    procesados++
                    if (procesados == pendientes.size) onComplete()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    procesados++
                    if (procesados == pendientes.size) onComplete()
                }
            })
        }
    }

    private fun borrarPendientesProveedor(api: ApiProveedor, onComplete: () -> Unit) {
        val pendientes = controllerProveedor.listarPendientesBorrar()

        if (pendientes.isEmpty()) {
            onComplete()
            return
        }

        var procesados = 0

        for (proveedor in pendientes) {
            api.eliminarProveedor(proveedor.idApi).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        controllerProveedor.eliminar(proveedor.cod)
                    }
                    procesados++
                    if (procesados == pendientes.size) onComplete()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    procesados++
                    if (procesados == pendientes.size) onComplete()
                }
            })
        }
    }

    private fun borrarPendientesProducto(api: ApiProducto, onComplete: () -> Unit) {
        val pendientes = controllerProducto.listarPendientesBorrar()

        if (pendientes.isEmpty()) {
            onComplete()
            return
        }

        var procesados = 0

        for (producto in pendientes) {
            api.eliminarProducto(producto.idApi).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        controllerProducto.eliminar(producto.cod)
                    }
                    procesados++
                    if (procesados == pendientes.size) onComplete()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    procesados++
                    if (procesados == pendientes.size) onComplete()
                }
            })
        }
    }

    private fun subirPendientesCategorias(api: ApiCategoria, onComplete: () -> Unit) {
        val pendientes = controllerCategoria.listarPendientes()

        if (pendientes.isEmpty()) {
            onComplete()
            return
        }

        var procesados = 0

        for (categoria in pendientes) {
            val call = if (categoria.idApi > 0) {
                api.actualizarCategoria(categoria.idApi, categoria)
            } else {
                api.crearCategoria(categoria)
            }

            call.enqueue(object : Callback<Categoria> {
                override fun onResponse(call: Call<Categoria>, response: Response<Categoria>) {
                    if (response.isSuccessful) {
                        val apiId = response.body()?.idApi ?: categoria.idApi
                        if (apiId > 0) {
                            controllerCategoria.marcarSincronizado(categoria.cod, apiId)
                            val catSync = categoria.copy(idApi = apiId, estadoSync = 1)
                            if (categoria.idApi == 0) {
                                bd.child("categorias").child(categoria.cod.toString()).removeValue()
                            }
                            bd.child("categorias").child(apiId.toString()).setValue(catSync)
                        }
                    }
                    procesados++
                    if (procesados == pendientes.size) onComplete()
                }

                override fun onFailure(call: Call<Categoria>, t: Throwable) {
                    procesados++
                    if (procesados == pendientes.size) onComplete()
                }
            })
        }
    }

    private fun subirPendientesProveedores(api: ApiProveedor, onComplete: () -> Unit) {
        val pendientes = controllerProveedor.listarPendientes()

        if (pendientes.isEmpty()) {
            onComplete()
            return
        }

        var procesados = 0

        for (proveedor in pendientes) {
            val call = if (proveedor.idApi > 0) {
                api.actualizarProveedor(proveedor.idApi, proveedor)
            } else {
                api.crearProveedor(proveedor)
            }

            call.enqueue(object : Callback<Proveedor> {
                override fun onResponse(call: Call<Proveedor>, response: Response<Proveedor>) {
                    if (response.isSuccessful) {
                        val apiId = response.body()?.idApi ?: proveedor.idApi
                        if (apiId > 0) {
                            controllerProveedor.marcarSincronizado(proveedor.cod, apiId)
                            val provSync = proveedor.copy(idApi = apiId, estadoSync = 1)
                            if (proveedor.idApi == 0) {
                                bd.child("proveedores").child(proveedor.cod.toString()).removeValue()
                            }
                            bd.child("proveedores").child(apiId.toString()).setValue(provSync)
                        }
                    }
                    procesados++
                    if (procesados == pendientes.size) onComplete()
                }

                override fun onFailure(call: Call<Proveedor>, t: Throwable) {
                    procesados++
                    if (procesados == pendientes.size) onComplete()
                }
            })
        }
    }

    private fun descargarCategorias(api: ApiCategoria, onComplete: () -> Unit) {
        api.listarCategorias().enqueue(object : Callback<List<Categoria>> {
            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    for (cat in lista) {
                        val existente = controllerCategoria.buscarPorIdApi(cat.idApi)
                        if (existente == null) {
                            controllerCategoria.insertarDesdeApi(cat)
                            bd.child("categorias").child(cat.idApi.toString()).setValue(cat)
                        } else {
                            controllerCategoria.actualizarDesdeApi(cat)
                        }
                    }
                }
                onComplete()
            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                onComplete()
            }
        })
    }

    private fun descargarProveedores(api: ApiProveedor, onComplete: () -> Unit) {
        api.listarProveedores().enqueue(object : Callback<List<Proveedor>> {
            override fun onResponse(call: Call<List<Proveedor>>, response: Response<List<Proveedor>>) {
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    for (prov in lista) {
                        val existente = controllerProveedor.buscarPorIdApi(prov.idApi)
                        if (existente == null) {
                            controllerProveedor.insertarDesdeApi(prov)
                            bd.child("proveedores").child(prov.idApi.toString()).setValue(prov)
                        } else {
                            controllerProveedor.actualizarDesdeApi(prov)
                        }
                    }
                }
                onComplete()
            }

            override fun onFailure(call: Call<List<Proveedor>>, t: Throwable) {
                onComplete()
            }
        })
    }

    private fun subirPendientesProductos(api: ApiProducto, onComplete: () -> Unit) {
        val pendientes = controllerProducto.listarPendientes()

        if (pendientes.isEmpty()) {
            onComplete()
            return
        }

        var procesados = 0
        var sincronizados = 0
        var errores = 0

        for (producto in pendientes) {
            val categoriaRef = if (producto.categoriaId > 0) {
                val cat = controllerCategoria.listar().firstOrNull { it.cod == producto.categoriaId }
                if (cat != null && cat.idApi > 0) CategoriaRef(id = cat.idApi, nombre = cat.nombre) else null
            } else null

            val proveedorRef = if (producto.proveedorId > 0) {
                val prov = controllerProveedor.listar().firstOrNull { it.cod == producto.proveedorId }
                if (prov != null && prov.idApi > 0) ProveedorRef(id = prov.idApi, nombre = prov.nombre) else null
            } else null

            val request = ProductoRequest(
                id = producto.idApi,
                nombre = producto.nombre,
                categoria = categoriaRef,
                proveedor = proveedorRef,
                precio = producto.precio,
                stock = producto.stock
            )

            val call = if (producto.idApi > 0) {
                api.actualizarProducto(producto.idApi, request)
            } else {
                api.crearProducto(request)
            }

            call.enqueue(object : Callback<Producto> {
                override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                    if (response.isSuccessful) {
                        val productoApi = response.body()
                        val apiId = productoApi?.idApi ?: 0
                        controllerProducto.marcarSincronizado(producto.cod, apiId)
                        val updates = mapOf<String, Any>("estadoSync" to 1)
                        bd.child("productos").child(producto.cod.toString()).updateChildren(updates)
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
                        val existente = controllerProducto.buscarPorIdApi(producto.idApi)
                        if (existente == null) {
                            controllerProducto.insertarDesdeApi(producto)
                            nuevos++
                        } else {
                            controllerProducto.actualizarDesdeApi(producto)
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
