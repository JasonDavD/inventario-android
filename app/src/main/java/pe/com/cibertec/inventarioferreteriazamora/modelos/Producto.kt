package pe.com.cibertec.inventarioferreteriazamora.modelos

import com.google.gson.annotations.SerializedName

data class CategoriaRef(
    @SerializedName("id") val id: Int = 0,
    val nombre: String = ""
)

data class ProveedorRef(
    @SerializedName("id") val id: Int = 0,
    val nombre: String = ""
)

data class Producto(
    var cod: Int = 0,
    @SerializedName("id") var idApi: Int = 0,
    var nombre: String = "",
    // Campos planos para SQLite y Firebase
    var categoriaId: Int = 0,
    var categoriaNombre: String = "",
    var proveedorId: Int = 0,
    var proveedorNombre: String = "",
    // Campos para deserializar JSON anidado del API
    @SerializedName("categoria") var categoriaApi: CategoriaRef? = null,
    @SerializedName("proveedor") var proveedorApi: ProveedorRef? = null,
    var precio: Double = 0.0,
    var stock: Int = 0,
    var estadoSync: Int = 0
)

data class ProductoRequest(
    @SerializedName("id") val id: Int = 0,
    val nombre: String,
    val categoria: CategoriaRef?,
    val proveedor: ProveedorRef?,
    val precio: Double,
    val stock: Int
)
