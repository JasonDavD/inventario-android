package pe.com.cibertec.inventarioferreteriazamora.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.adaptador.ProductoAdapter
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerProducto

class ListaProductoActivity : AppCompatActivity() {

    private lateinit var rvProductos: RecyclerView
    private lateinit var tvVacio: TextView
    private lateinit var adapter: ProductoAdapter
    private val controller = ControllerProducto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_producto)

        rvProductos = findViewById(R.id.rvProductos)
        tvVacio = findViewById(R.id.tvVacio)

        rvProductos.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter(
            arrayListOf(),
            onEditar = { producto ->
                val intent = Intent(this, NuevoProductoActivity::class.java).apply {
                    putExtra("cod", producto.cod)
                    putExtra("nombre", producto.nombre)
                    putExtra("categoria", producto.categoria)
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
                            Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
                            cargarProductos()
                        } else {
                            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
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
}
