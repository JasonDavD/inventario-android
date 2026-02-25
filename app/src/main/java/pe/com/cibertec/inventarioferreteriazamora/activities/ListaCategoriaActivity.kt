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
import pe.com.cibertec.inventarioferreteriazamora.adaptador.CategoriaAdapter
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerCategoria
import pe.com.cibertec.inventarioferreteriazamora.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaCategoriaActivity : AppCompatActivity() {

    private lateinit var rvCategorias: RecyclerView
    private lateinit var tvVacioCategoria: TextView
    private lateinit var fabNuevaCategoria: FloatingActionButton
    private lateinit var adapter: CategoriaAdapter
    private val controller = ControllerCategoria()
    private lateinit var bd: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_categoria)

        FirebaseApp.initializeApp(this)
        bd = FirebaseDatabase.getInstance().reference

        rvCategorias = findViewById(R.id.rvCategorias)
        tvVacioCategoria = findViewById(R.id.tvVacioCategoria)
        fabNuevaCategoria = findViewById(R.id.fabNuevaCategoria)

        fabNuevaCategoria.setOnClickListener {
            startActivity(Intent(this, NuevaCategoriaActivity::class.java))
        }

        rvCategorias.layoutManager = LinearLayoutManager(this)

        adapter = CategoriaAdapter(
            arrayListOf(),
            onEditar = { categoria ->
                val intent = Intent(this, EditarCategoriaActivity::class.java).apply {
                    putExtra("cod", categoria.cod)
                    putExtra("idApi", categoria.idApi)
                    putExtra("nombre", categoria.nombre)
                    putExtra("descripcion", categoria.descripcion)
                }
                startActivity(intent)
            },
            onEliminar = { categoria ->
                MaterialAlertDialogBuilder(this)
                    .setTitle("Eliminar categoria")
                    .setMessage("¿Esta seguro de eliminar '${categoria.nombre}'?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        val resultado = controller.eliminar(categoria.cod)
                        if (resultado > 0) {
                            bd.child("categorias").child(categoria.cod.toString()).removeValue()
                            // Eliminar en el API si tiene idApi
                            if (categoria.idApi > 0) {
                                ApiUtils.getAPICategoria().eliminarCategoria(categoria.idApi)
                                    .enqueue(object : Callback<Void> {
                                        override fun onResponse(call: Call<Void>, response: Response<Void>) {}
                                        override fun onFailure(call: Call<Void>, t: Throwable) {}
                                    })
                                bd.child("categorias").child(categoria.idApi.toString()).removeValue()
                            }
                            showAlert("Categoria eliminada")
                            cargarCategorias()
                        } else {
                            showAlert("Error al eliminar")
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        rvCategorias.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        cargarCategorias()
    }

    private fun cargarCategorias() {
        val lista = controller.listar()
        adapter.actualizarLista(lista)

        if (lista.isEmpty()) {
            tvVacioCategoria.visibility = View.VISIBLE
            rvCategorias.visibility = View.GONE
        } else {
            tvVacioCategoria.visibility = View.GONE
            rvCategorias.visibility = View.VISIBLE
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
