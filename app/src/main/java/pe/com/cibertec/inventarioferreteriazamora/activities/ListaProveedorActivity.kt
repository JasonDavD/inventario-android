package pe.com.cibertec.inventarioferreteriazamora.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.adaptador.ProveedorAdapter
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerProveedor
import pe.com.cibertec.inventarioferreteriazamora.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaProveedorActivity : AppCompatActivity() {

    private lateinit var rvProveedores: RecyclerView
    private lateinit var tvVacioProveedor: TextView
    private lateinit var fabNuevoProveedor: FloatingActionButton
    private lateinit var adapter: ProveedorAdapter
    private val controller = ControllerProveedor()
    private lateinit var bd: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_proveedor)

        FirebaseApp.initializeApp(this)
        bd = FirebaseDatabase.getInstance().reference

        rvProveedores = findViewById(R.id.rvProveedores)
        tvVacioProveedor = findViewById(R.id.tvVacioProveedor)
        fabNuevoProveedor = findViewById(R.id.fabNuevoProveedor)

        fabNuevoProveedor.setOnClickListener {
            startActivity(Intent(this, NuevoProveedorActivity::class.java))
        }

        rvProveedores.layoutManager = LinearLayoutManager(this)

        adapter = ProveedorAdapter(
            arrayListOf(),
            onEditar = { proveedor ->
                val intent = Intent(this, EditarProveedorActivity::class.java).apply {
                    putExtra("cod", proveedor.cod)
                    putExtra("idApi", proveedor.idApi)
                    putExtra("nombre", proveedor.nombre)
                    putExtra("telefono", proveedor.telefono)
                    putExtra("direccion", proveedor.direccion)
                }
                startActivity(intent)
            },
            onEliminar = { proveedor ->
                MaterialAlertDialogBuilder(this)
                    .setTitle("Eliminar proveedor")
                    .setMessage("¿Esta seguro de eliminar '${proveedor.nombre}'?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        val resultado = controller.eliminar(proveedor.cod)
                        if (resultado > 0) {
                            bd.child("proveedores").child(proveedor.cod.toString()).removeValue()
                            // Eliminar en el API si tiene idApi
                            if (proveedor.idApi > 0) {
                                ApiUtils.getAPIProveedor().eliminarProveedor(proveedor.idApi)
                                    .enqueue(object : Callback<Void> {
                                        override fun onResponse(call: Call<Void>, response: Response<Void>) {}
                                        override fun onFailure(call: Call<Void>, t: Throwable) {}
                                    })
                                bd.child("proveedores").child(proveedor.idApi.toString()).removeValue()
                            }
                            showAlert("Proveedor eliminado")
                            cargarProveedores()
                        } else {
                            showAlert("Error al eliminar")
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        rvProveedores.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        cargarProveedores()
    }

    private fun cargarProveedores() {
        val lista = controller.listar()
        adapter.actualizarLista(lista)

        if (lista.isEmpty()) {
            tvVacioProveedor.visibility = View.VISIBLE
            rvProveedores.visibility = View.GONE
        } else {
            tvVacioProveedor.visibility = View.GONE
            rvProveedores.visibility = View.VISIBLE
        }
    }

    fun showAlert(men: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("SISTEMA")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar", null)
        builder.create().show()
    }
}
