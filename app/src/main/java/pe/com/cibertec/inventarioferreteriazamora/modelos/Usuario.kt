package pe.com.cibertec.inventarioferreteriazamora.modelos

data class Usuario(
    var cod: Int = 0,
    var usuario: String = "",
    var contrasena: String = "",
    var nombre: String = "",
    var rol: String = ""
)
