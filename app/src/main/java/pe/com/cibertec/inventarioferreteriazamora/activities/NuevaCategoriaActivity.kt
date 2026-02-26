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

class NuevaCategoriaActivity : AppCompatActivity() {

    private lateinit var txtNombreCategoria: TextInputEditText
    private lateinit var txtDescripcionCategoria: TextInputEditText
    private lateinit var btnGuardarCategoria: MaterialButton

    private val controller = ControllerCategoria()
    private lateinit var bd: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_categoria)

        txtNombreCategoria = findViewById(R.id.txtNombreCategoria)
        txtDescripcionCategoria = findViewById(R.id.txtDescripcionCategoria)
        btnGuardarCategoria = findViewById(R.id.btnGuardarCategoria)

        FirebaseApp.initializeApp(this)
        bd = FirebaseDatabase.getInstance().reference

        btnGuardarCategoria.setOnClickListener {
            guardarCategoria()
        }
    }

    private fun guardarCategoria() {
        val nombre = txtNombreCategoria.text.toString().trim()
        val descripcion = txtDescripcionCategoria.text.toString().trim()

        if (nombre.isEmpty()) {
            txtNombreCategoria.error = "Ingrese el nombre"
            txtNombreCategoria.requestFocus()
            return
        }

        val categoria = Categoria(nombre = nombre, descripcion = descripcion)
        val resultado = controller.insertar(categoria)

        if (resultado > 0) {
            val cod = resultado.toInt()
            val catLocal = categoria.copy(cod = cod)
            bd.child("categorias").child(cod.toString()).setValue(catLocal)
            showAlert("Categoria guardada")
            finish()
        } else {
            showAlert("Error al guardar")
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
