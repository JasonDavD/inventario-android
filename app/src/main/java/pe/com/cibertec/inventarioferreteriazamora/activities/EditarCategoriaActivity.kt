package pe.com.cibertec.inventarioferreteriazamora.activities

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerCategoria
import pe.com.cibertec.inventarioferreteriazamora.modelos.Categoria

class EditarCategoriaActivity : AppCompatActivity() {

    private lateinit var txtNombreCategoria: TextInputEditText
    private lateinit var txtDescripcionCategoria: TextInputEditText
    private lateinit var btnActualizarCategoria: MaterialButton

    private val controller = ControllerCategoria()
    private lateinit var bd: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_categoria)

        FirebaseApp.initializeApp(this)
        bd = FirebaseDatabase.getInstance().reference

        txtNombreCategoria = findViewById(R.id.txtNombreCategoria)
        txtDescripcionCategoria = findViewById(R.id.txtDescripcionCategoria)
        btnActualizarCategoria = findViewById(R.id.btnActualizarCategoria)

        txtNombreCategoria.setText(intent.getStringExtra("nombre"))
        txtDescripcionCategoria.setText(intent.getStringExtra("descripcion"))

        btnActualizarCategoria.setOnClickListener {
            actualizarCategoria()
        }
    }

    private fun actualizarCategoria() {
        val nombre = txtNombreCategoria.text.toString().trim()
        val descripcion = txtDescripcionCategoria.text.toString().trim()

        if (nombre.isEmpty()) {
            txtNombreCategoria.error = "Ingrese el nombre"
            txtNombreCategoria.requestFocus()
            return
        }

        val categoria = Categoria(
            cod = intent.getIntExtra("cod", 0),
            idApi = intent.getIntExtra("idApi", 0),
            nombre = nombre,
            descripcion = descripcion
        )

        val resultado = controller.actualizar(categoria)
        if (resultado > 0) {
            bd.child("categorias").child(categoria.cod.toString()).setValue(categoria)
            showAlert("Categoria actualizada")
            finish()
        } else {
            showAlert("Error al actualizar")
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
