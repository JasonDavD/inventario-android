package pe.com.cibertec.inventarioferreteriazamora.controller

import android.content.ContentValues
import pe.com.cibertec.inventarioferreteriazamora.modelos.Proveedor
import pe.com.cibertec.inventarioferreteriazamora.utils.AppConfig

class ControllerProveedor {

    fun insertar(proveedor: Proveedor): Long {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", proveedor.nombre)
            put("telefono", proveedor.telefono)
            put("direccion", proveedor.direccion)
            put("estado_sync", 0)
            put("id_api", proveedor.idApi)
        }
        val resultado = db.insert("tb_proveedor", null, valores)
        db.close()
        return resultado
    }

    fun listar(): ArrayList<Proveedor> {
        val lista = ArrayList<Proveedor>()
        val db = AppConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_proveedor", null)
        while (cursor.moveToNext()) {
            lista.add(Proveedor(
                cod = cursor.getInt(0),
                nombre = cursor.getString(1),
                telefono = cursor.getString(2) ?: "",
                direccion = cursor.getString(3) ?: "",
                estadoSync = cursor.getInt(4),
                idApi = cursor.getInt(5)
            ))
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizar(proveedor: Proveedor): Int {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", proveedor.nombre)
            put("telefono", proveedor.telefono)
            put("direccion", proveedor.direccion)
            put("estado_sync", 0)
        }
        val resultado = db.update("tb_proveedor", valores, "cod=?", arrayOf(proveedor.cod.toString()))
        db.close()
        return resultado
    }

    fun eliminar(cod: Int): Int {
        val db = AppConfig.BD.writableDatabase
        val resultado = db.delete("tb_proveedor", "cod=?", arrayOf(cod.toString()))
        db.close()
        return resultado
    }

    fun buscarPorIdApi(idApi: Int): Proveedor? {
        val db = AppConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_proveedor WHERE id_api = ?", arrayOf(idApi.toString()))
        var proveedor: Proveedor? = null
        if (cursor.moveToFirst()) {
            proveedor = Proveedor(
                cod = cursor.getInt(0),
                nombre = cursor.getString(1),
                telefono = cursor.getString(2) ?: "",
                direccion = cursor.getString(3) ?: "",
                estadoSync = cursor.getInt(4),
                idApi = cursor.getInt(5)
            )
        }
        cursor.close()
        db.close()
        return proveedor
    }

    fun insertarDesdeApi(proveedor: Proveedor): Long {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", proveedor.nombre)
            put("telefono", proveedor.telefono)
            put("direccion", proveedor.direccion)
            put("estado_sync", 1)
            put("id_api", proveedor.idApi)
        }
        val resultado = db.insert("tb_proveedor", null, valores)
        db.close()
        return resultado
    }

    fun actualizarDesdeApi(proveedor: Proveedor): Int {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", proveedor.nombre)
            put("telefono", proveedor.telefono)
            put("direccion", proveedor.direccion)
            put("estado_sync", 1)
        }
        val resultado = db.update("tb_proveedor", valores, "id_api=?", arrayOf(proveedor.idApi.toString()))
        db.close()
        return resultado
    }

    fun listarPendientes(): ArrayList<Proveedor> {
        val lista = ArrayList<Proveedor>()
        val db = AppConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_proveedor WHERE estado_sync = 0", null)
        while (cursor.moveToNext()) {
            lista.add(Proveedor(
                cod = cursor.getInt(0),
                nombre = cursor.getString(1),
                telefono = cursor.getString(2) ?: "",
                direccion = cursor.getString(3) ?: "",
                estadoSync = cursor.getInt(4),
                idApi = cursor.getInt(5)
            ))
        }
        cursor.close()
        db.close()
        return lista
    }

    fun marcarSincronizado(cod: Int, idApi: Int): Int {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("estado_sync", 1)
            put("id_api", idApi)
        }
        val resultado = db.update("tb_proveedor", valores, "cod=?", arrayOf(cod.toString()))
        db.close()
        return resultado
    }
}
