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
import pe.com.cibertec.inventarioferreteriazamora.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarProveedorActivity : AppCompatActivity() {

    private lateinit var txtNombreProveedor: TextInputEditText
    private lateinit var txtTelefonoProveedor: TextInputEditText
    private lateinit var txtDireccionProveedor: TextInputEditText
    private lateinit var btnActualizarProveedor: MaterialButton

    private val controller = ControllerProveedor()
    private lateinit var bd: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_proveedor)

        FirebaseApp.initializeApp(this)
        bd = FirebaseDatabase.getInstance().reference

        txtNombreProveedor = findViewById(R.id.txtNombreProveedor)
        txtTelefonoProveedor = findViewById(R.id.txtTelefonoProveedor)
        txtDireccionProveedor = findViewById(R.id.txtDireccionProveedor)
        btnActualizarProveedor = findViewById(R.id.btnActualizarProveedor)

        txtNombreProveedor.setText(intent.getStringExtra("nombre"))
        txtTelefonoProveedor.setText(intent.getStringExtra("telefono"))
        txtDireccionProveedor.setText(intent.getStringExtra("direccion"))

        btnActualizarProveedor.setOnClickListener {
            actualizarProveedor()
        }
    }

    private fun actualizarProveedor() {
        val nombre = txtNombreProveedor.text.toString().trim()
        val telefono = txtTelefonoProveedor.text.toString().trim()
        val direccion = txtDireccionProveedor.text.toString().trim()

        if (nombre.isEmpty()) {
            txtNombreProveedor.error = "Ingrese el nombre"
            txtNombreProveedor.requestFocus()
            return
        }

        val proveedor = Proveedor(
            cod = intent.getIntExtra("cod", 0),
            idApi = intent.getIntExtra("idApi", 0),
            nombre = nombre,
            telefono = telefono,
            direccion = direccion
        )

        val resultado = controller.actualizar(proveedor)
        if (resultado > 0) {
            // Actualizar Firebase
            bd.child("proveedores").child(proveedor.cod.toString()).setValue(proveedor)

            // Llamar al API si ya tiene idApi
            if (proveedor.idApi > 0) {
                ApiUtils.getAPIProveedor().actualizarProveedor(proveedor.idApi, proveedor)
                    .enqueue(object : Callback<Proveedor> {
                        override fun onResponse(call: Call<Proveedor>, response: Response<Proveedor>) {
                            if (response.isSuccessful) {
                                controller.marcarSincronizado(proveedor.cod, proveedor.idApi)
                                val provSync = proveedor.copy(estadoSync = 1)
                                bd.child("proveedores").child(proveedor.idApi.toString()).setValue(provSync)
                            }
                            showAlert("Proveedor actualizado")
                            finish()
                        }

                        override fun onFailure(call: Call<Proveedor>, t: Throwable) {
                            showAlert("Actualizado localmente. Se sincronizará luego.")
                            finish()
                        }
                    })
            } else {
                showAlert("Proveedor actualizado")
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
