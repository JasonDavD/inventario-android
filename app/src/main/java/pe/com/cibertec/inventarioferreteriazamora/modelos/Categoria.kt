package pe.com.cibertec.inventarioferreteriazamora.modelos

import com.google.gson.annotations.SerializedName

data class Categoria(
    var cod: Int = 0,
    @SerializedName("id") var idApi: Int = 0,
    var nombre: String = "",
    var descripcion: String = "",
    var estadoSync: Int = 0
)
