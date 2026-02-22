package pe.com.cibertec.inventarioferreteriazamora.activities

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerProducto
import pe.com.cibertec.inventarioferreteriazamora.modelos.Producto

class EditarProductoActivity : AppCompatActivity() {

    private lateinit var txtNombre: TextInputEditText
    private lateinit var txtCategoria: TextInputEditText
    private lateinit var txtPrecio: TextInputEditText
    private lateinit var txtStock: TextInputEditText
    private lateinit var btnActualizar: MaterialButton

    private val controller = ControllerProducto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        txtNombre = findViewById(R.id.txtNombre)
        txtCategoria = findViewById(R.id.txtCategoria)
        txtPrecio = findViewById(R.id.txtPrecio)
        txtStock = findViewById(R.id.txtStock)
        btnActualizar = findViewById(R.id.btnActualizar)

        txtNombre.setText(intent.getStringExtra("nombre"))
        txtCategoria.setText(intent.getStringExtra("categoria"))
        txtPrecio.setText(intent.getDoubleExtra("precio", 0.0).toString())
        txtStock.setText(intent.getIntExtra("stock", 0).toString())

        btnActualizar.setOnClickListener {
            actualizarProducto()
        }
    }

    private fun actualizarProducto() {
        val nombre = txtNombre.text.toString().trim()
        val categoria = txtCategoria.text.toString().trim()
        val precioStr = txtPrecio.text.toString().trim()
        val stockStr = txtStock.text.toString().trim()

        if (nombre.isEmpty()) {
            txtNombre.error = "Ingrese el nombre"
            txtNombre.requestFocus()
            return
        }
        if (precioStr.isEmpty()) {
            txtPrecio.error = "Ingrese el precio"
            txtPrecio.requestFocus()
            return
        }
        if (stockStr.isEmpty()) {
            txtStock.error = "Ingrese el stock"
            txtStock.requestFocus()
            return
        }

        val precio = precioStr.toDoubleOrNull()
        val stock = stockStr.toIntOrNull()

        if (precio == null || precio < 0) {
            txtPrecio.error = "Precio no valido"
            txtPrecio.requestFocus()
            return
        }
        if (stock == null || stock < 0) {
            txtStock.error = "Stock no valido"
            txtStock.requestFocus()
            return
        }

        val producto = Producto(
            cod = intent.getIntExtra("cod", 0),
            idApi = intent.getIntExtra("idApi", 0),
            nombre = nombre,
            categoria = categoria,
            precio = precio,
            stock = stock
        )

        val resultado = controller.actualizar(producto)
        if (resultado > 0) {
            showAlert("Producto actualizado")
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
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
