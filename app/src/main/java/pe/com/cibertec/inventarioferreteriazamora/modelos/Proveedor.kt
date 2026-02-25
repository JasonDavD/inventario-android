package pe.com.cibertec.inventarioferreteriazamora.modelos

import com.google.gson.annotations.SerializedName

data class Proveedor(
    var cod: Int = 0,
    @SerializedName("id") var idApi: Int = 0,
    var nombre: String = "",
    var telefono: String = "",
    var direccion: String = "",
    var estadoSync: Int = 0
)
