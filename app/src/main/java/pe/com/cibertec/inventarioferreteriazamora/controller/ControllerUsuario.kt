package pe.com.cibertec.inventarioferreteriazamora.controller

import pe.com.cibertec.inventarioferreteriazamora.data.InitBD
import pe.com.cibertec.inventarioferreteriazamora.modelos.Usuario

class ControllerUsuario {

    fun buscarPorCredenciales(usuario: String, contrasena: String): Usuario? {
        val db = InitBD().readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM tb_usuario WHERE usuario = ? AND contrasena = ?",
            arrayOf(usuario, contrasena)
        )
        var resultado: Usuario? = null
        if (cursor.moveToFirst()) {
            resultado = Usuario(
                cod = cursor.getInt(0),
                usuario = cursor.getString(1),
                contrasena = cursor.getString(2),
                nombre = cursor.getString(3),
                rol = cursor.getString(4)
            )
        }
        cursor.close()
        db.close()
        return resultado
    }
}
