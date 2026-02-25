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

class EditarProductoActivity : AppCompatActivity() {

    private lateinit var txtNombre: TextInputEditText
    private lateinit var spinnerCategoria: Spinner
    private lateinit var spinnerProveedor: Spinner
    private lateinit var txtPrecio: TextInputEditText
    private lateinit var txtStock: TextInputEditText
    private lateinit var btnActualizar: MaterialButton

    private val controller = ControllerProducto()
    private val controllerCat = ControllerCategoria()
    private val controllerProv = ControllerProveedor()
    private lateinit var bd: DatabaseReference

    private var categorias = ArrayList<Categoria>()
    private var proveedores = ArrayList<Proveedor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        FirebaseApp.initializeApp(this)
        bd = FirebaseDatabase.getInstance().reference

        txtNombre = findViewById(R.id.txtNombre)
        spinnerCategoria = findViewById(R.id.spinnerCategoria)
        spinnerProveedor = findViewById(R.id.spinnerProveedor)
        txtPrecio = findViewById(R.id.txtPrecio)
        txtStock = findViewById(R.id.txtStock)
        btnActualizar = findViewById(R.id.btnActualizar)

        categorias = controllerCat.listar()
        proveedores = controllerProv.listar()

        val adapterCat = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            categorias.map { it.nombre })
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapterCat

        val adapterProv = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            proveedores.map { it.nombre })
        adapterProv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProveedor.adapter = adapterProv

        // Pre-seleccionar categoria y proveedor actuales
        val categoriaIdActual = intent.getIntExtra("categoriaId", 0)
        val proveedorIdActual = intent.getIntExtra("proveedorId", 0)

        val posCat = categorias.indexOfFirst { it.cod == categoriaIdActual }
        if (posCat >= 0) spinnerCategoria.setSelection(posCat)

        val posProv = proveedores.indexOfFirst { it.cod == proveedorIdActual }
        if (posProv >= 0) spinnerProveedor.setSelection(posProv)

        txtNombre.setText(intent.getStringExtra("nombre"))
        txtPrecio.setText(intent.getDoubleExtra("precio", 0.0).toString())
        txtStock.setText(intent.getIntExtra("stock", 0).toString())

        btnActualizar.setOnClickListener {
            actualizarProducto()
        }
    }

    private fun actualizarProducto() {
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

        val categoriaSeleccionada = if (categorias.isNotEmpty())
            categorias[spinnerCategoria.selectedItemPosition] else Categoria()
        val proveedorSeleccionado = if (proveedores.isNotEmpty())
            proveedores[spinnerProveedor.selectedItemPosition] else Proveedor()

        val producto = Producto(
            cod = intent.getIntExtra("cod", 0),
            idApi = intent.getIntExtra("idApi", 0),
            nombre = nombre,
            categoriaId = categoriaSeleccionada.cod,
            categoriaNombre = categoriaSeleccionada.nombre,
            proveedorId = proveedorSeleccionado.cod,
            proveedorNombre = proveedorSeleccionado.nombre,
            precio = precio,
            stock = stock
        )

        val resultado = controller.actualizar(producto)
        if (resultado > 0) {
            bd.child("productos").child(producto.cod.toString()).setValue(producto)
                .addOnCompleteListener {
                    showAlert("Producto actualizado")
                    finish()
                }.addOnFailureListener {
                    showAlert("Producto actualizado localmente (sin Firebase)")
                    finish()
                }
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
