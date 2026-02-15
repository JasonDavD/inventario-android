package pe.com.cibertec.inventarioferreteriazamora.modelos

data class Producto(
    var cod: Int = 0,
    var nombre: String = "",
    var categoria: String = "",
    var precio: Double = 0.0,
    var stock: Int = 0,
    var estadoSync: Int = 0  // 0 = pendiente, 1 = sincronizado
)
