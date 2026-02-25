package pe.com.cibertec.inventarioferreteriazamora.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.adaptador.ProductoAdapter
import pe.com.cibertec.inventarioferreteriazamora.modelos.Producto

class ListaFirebaseActivity : AppCompatActivity() {

    private lateinit var rvFirebase: RecyclerView
    private lateinit var tvVacio: TextView
    private lateinit var bd: DatabaseReference
    private lateinit var lista: ArrayList<Producto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_firebase)

        rvFirebase = findViewById(R.id.rvFirebase)
        tvVacio = findViewById(R.id.tvVacio)

        FirebaseApp.initializeApp(this)
        bd = FirebaseDatabase.getInstance().reference

        listar()
    }

    fun listar() {
        lista = ArrayList()
        bd.child("productos").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                for (row in snapshot.children) {
                    val bean = row.getValue(Producto::class.java)
                    lista.add(bean!!)
                }
                val adaptador = ProductoAdapter(lista, onEditar = {}, onEliminar = {})
                rvFirebase.layoutManager = LinearLayoutManager(this@ListaFirebaseActivity)
                rvFirebase.adapter = adaptador

                if (lista.isEmpty()) {
                    tvVacio.visibility = View.VISIBLE
                    rvFirebase.visibility = View.GONE
                } else {
                    tvVacio.visibility = View.GONE
                    rvFirebase.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showAlert(error.message)
            }
        })
    }

    fun showAlert(men: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("SISTEMA")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar", null)
        builder.create().show()
    }
}
