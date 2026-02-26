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
import pe.com.cibertec.inventarioferreteriazamora.controller.ControllerProveedor
import pe.com.cibertec.inventarioferreteriazamora.modelos.Proveedor

class NuevoProveedorActivity : AppCompatActivity() {

    private lateinit var txtNombreProveedor: TextInputEditText
    private lateinit var txtTelefonoProveedor: TextInputEditText
    private lateinit var txtDireccionProveedor: TextInputEditText
    private lateinit var btnGuardarProveedor: MaterialButton

    private val controller = ControllerProveedor()
    private lateinit var bd: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_proveedor)

        txtNombreProveedor = findViewById(R.id.txtNombreProveedor)
        txtTelefonoProveedor = findViewById(R.id.txtTelefonoProveedor)
        txtDireccionProveedor = findViewById(R.id.txtDireccionProveedor)
        btnGuardarProveedor = findViewById(R.id.btnGuardarProveedor)

        FirebaseApp.initializeApp(this)
        bd = FirebaseDatabase.getInstance().reference

        btnGuardarProveedor.setOnClickListener {
            guardarProveedor()
        }
    }

    private fun guardarProveedor() {
        val nombre = txtNombreProveedor.text.toString().trim()
        val telefono = txtTelefonoProveedor.text.toString().trim()
        val direccion = txtDireccionProveedor.text.toString().trim()

        if (nombre.isEmpty()) {
            txtNombreProveedor.error = "Ingrese el nombre"
            txtNombreProveedor.requestFocus()
            return
        }

        val proveedor = Proveedor(nombre = nombre, telefono = telefono, direccion = direccion)
        val resultado = controller.insertar(proveedor)

        if (resultado > 0) {
            val cod = resultado.toInt()
            val provLocal = proveedor.copy(cod = cod)
            bd.child("proveedores").child(cod.toString()).setValue(provLocal)
            showAlert("Proveedor guardado")
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
