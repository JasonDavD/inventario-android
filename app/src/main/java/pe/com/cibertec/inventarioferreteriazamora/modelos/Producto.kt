package pe.com.cibertec.inventarioferreteriazamora.modelos

import com.google.gson.annotations.SerializedName

data class Producto(
    var cod: Int = 0,
    @SerializedName("id")
    var idApi: Int = 0,
    var nombre: String = "",
    var categoria: String = "",
    var precio: Double = 0.0,
    var stock: Int = 0,
    var estadoSync: Int = 0  // 0 = pendiente, 1 = sincronizado
)
