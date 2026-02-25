package pe.com.cibertec.inventarioferreteriazamora.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerCategoria
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerProducto
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerProveedor
import pe.com.cibertec.inventarioferreteriazamora.modelos.Categoria
import pe.com.cibertec.inventarioferreteriazamora.modelos.Producto
import pe.com.cibertec.inventarioferreteriazamora.modelos.Proveedor

class NuevoProductoActivity : AppCompatActivity() {

    private lateinit var txtNombre: TextInputEditText
    private lateinit var spinnerCategoria: Spinner
    private lateinit var spinnerProveedor: Spinner
    private lateinit var txtPrecio: TextInputEditText
    private lateinit var txtStock: TextInputEditText
    private lateinit var btnGuardar: MaterialButton

    private val controller = ControllerProducto()
    private val controllerCat = ControllerCategoria()
    private val controllerProv = ControllerProveedor()
    private lateinit var bd: DatabaseReference

    private var categorias = ArrayList<Categoria>()
    private var proveedores = ArrayList<Proveedor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_producto)

        txtNombre = findViewById(R.id.txtNombre)
        spinnerCategoria = findViewById(R.id.spinnerCategoria)
        spinnerProveedor = findViewById(R.id.spinnerProveedor)
        txtPrecio = findViewById(R.id.txtPrecio)
        txtStock = findViewById(R.id.txtStock)
        btnGuardar = findViewById(R.id.btnGuardar)

        FirebaseApp.initializeApp(this)
        bd = FirebaseDatabase.getInstance().reference

        categorias = controllerCat.listar()
        proveedores = controllerProv.listar()

        if (categorias.isEmpty() || proveedores.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Datos requeridos")
                .setMessage("No hay categorias o proveedores disponibles.\nSincroniza o agrega datos primero.")
                .setPositiveButton("Aceptar") { _, _ -> finish() }
                .setCancelable(false)
                .show()
            return
        }

        val adapterCat = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            categorias.map { it.nombre })
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapterCat

        val adapterProv = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            proveedores.map { it.nombre })
        adapterProv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProveedor.adapter = adapterProv

        btnGuardar.setOnClickListener {
            guardarProducto()
        }
    }

    private fun guardarProducto() {
        val nombre = txtNombre.text.toString().trim()
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

        val categoriaSeleccionada = categorias[spinnerCategoria.selectedItemPosition]
        val proveedorSeleccionado = proveedores[spinnerProveedor.selectedItemPosition]

        val producto = Producto(
            nombre = nombre,
            categoriaId = categoriaSeleccionada.cod,
            categoriaNombre = categoriaSeleccionada.nombre,
            proveedorId = proveedorSeleccionado.cod,
            proveedorNombre = proveedorSeleccionado.nombre,
            precio = precio,
            stock = stock
        )

        val resultado = controller.insertar(producto)
        if (resultado > 0) {
            val productoFirebase = producto.copy(cod = resultado.toInt())
            bd.child("productos").child(resultado.toString()).setValue(productoFirebase)
                .addOnCompleteListener {
                    showAlert("Producto guardado")
                    finish()
                }.addOnFailureListener {
                    showAlert("Producto guardado localmente (sin Firebase)")
                    finish()
                }
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
