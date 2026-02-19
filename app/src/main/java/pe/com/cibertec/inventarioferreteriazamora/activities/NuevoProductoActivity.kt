package pe.com.cibertec.inventarioferreteriazamora.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerProducto
import pe.com.cibertec.inventarioferreteriazamora.modelos.Producto

class NuevoProductoActivity : AppCompatActivity() {

    private lateinit var txtNombre: TextInputEditText
    private lateinit var txtCategoria: TextInputEditText
    private lateinit var txtPrecio: TextInputEditText
    private lateinit var txtStock: TextInputEditText
    private lateinit var btnGuardar: MaterialButton

    private val controller = ControllerProducto()
    private var codEditar: Int = -1
    private var idApiEditar: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_producto)

        txtNombre = findViewById(R.id.txtNombre)
        txtCategoria = findViewById(R.id.txtCategoria)
        txtPrecio = findViewById(R.id.txtPrecio)
        txtStock = findViewById(R.id.txtStock)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Verificar si es modo edicion
        codEditar = intent.getIntExtra("cod", -1)
        idApiEditar = intent.getIntExtra("idApi", 0)
        if (codEditar != -1) {
            cargarDatosEdicion()
        }

        btnGuardar.setOnClickListener {
            guardarProducto()
        }
    }

    private fun cargarDatosEdicion() {
        txtNombre.setText(intent.getStringExtra("nombre"))
        txtCategoria.setText(intent.getStringExtra("categoria"))
        txtPrecio.setText(intent.getDoubleExtra("precio", 0.0).toString())
        txtStock.setText(intent.getIntExtra("stock", 0).toString())
        btnGuardar.text = "Actualizar"
    }

    private fun guardarProducto() {
        val nombre = txtNombre.text.toString().trim()
        val categoria = txtCategoria.text.toString().trim()
        val precioStr = txtPrecio.text.toString().trim()
        val stockStr = txtStock.text.toString().trim()

        // Validaciones
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
            nombre = nombre,
            categoria = categoria,
            precio = precio,
            stock = stock
        )

        if (codEditar != -1) {
            producto.cod = codEditar
            producto.idApi = idApiEditar
            val resultado = controller.actualizar(producto)
            if (resultado > 0) {
                showAlert("Producto actualizado")
                finish()
            } else {
                showAlert("Error al actualizar")
            }
        } else {
            val resultado = controller.insertar(producto)
            if (resultado > 0) {
                showAlert("Producto guardado")
                finish()
            } else {
                showAlert("Error al guardar")
            }
        }
    }
    fun showAlert(men:String){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("SISTEMA")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }

}
