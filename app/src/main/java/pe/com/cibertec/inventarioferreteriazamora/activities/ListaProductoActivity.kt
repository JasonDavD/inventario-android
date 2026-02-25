package pe.com.cibertec.inventarioferreteriazamora.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.adaptador.ProductoAdapter
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerProducto

class ListaProductoActivity : AppCompatActivity() {

    private lateinit var rvProductos: RecyclerView
    private lateinit var tvVacio: TextView
    private lateinit var adapter: ProductoAdapter
    private val controller = ControllerProducto()
    private lateinit var bd: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_producto)

        FirebaseApp.initializeApp(this)
        bd = FirebaseDatabase.getInstance().reference

        rvProductos = findViewById(R.id.rvProductos)
        tvVacio = findViewById(R.id.tvVacio)

        rvProductos.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter(
            arrayListOf(),
            onEditar = { producto ->
                val intent = Intent(this, EditarProductoActivity::class.java).apply {
                    putExtra("cod", producto.cod)
                    putExtra("idApi", producto.idApi)
                    putExtra("nombre", producto.nombre)
                    putExtra("categoriaId", producto.categoriaId)
                    putExtra("proveedorId", producto.proveedorId)
                    putExtra("precio", producto.precio)
                    putExtra("stock", producto.stock)
                }
                startActivity(intent)
            },
            onEliminar = { producto ->
                MaterialAlertDialogBuilder(this)
                    .setTitle("Eliminar producto")
                    .setMessage("¿Esta seguro de eliminar '${producto.nombre}'?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        val resultado = controller.eliminar(producto.cod)
                        if (resultado > 0) {
                            bd.child("productos").child(producto.cod.toString()).removeValue()
                            showAlert("Producto eliminado")
                            cargarProductos()
                        } else {
                            showAlert("Error al eliminar")
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        rvProductos.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    private fun cargarProductos() {
        val lista = controller.listar()
        adapter.actualizarLista(lista)

        if (lista.isEmpty()) {
            tvVacio.visibility = View.VISIBLE
            rvProductos.visibility = View.GONE
        } else {
            tvVacio.visibility = View.GONE
            rvProductos.visibility = View.VISIBLE
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
