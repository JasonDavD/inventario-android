package pe.com.cibertec.inventarioferreteriazamora.controller

import android.content.ContentValues
import pe.com.cibertec.inventarioferreteriazamora.modelos.Categoria
import pe.com.cibertec.inventarioferreteriazamora.utils.AppConfig

class ControllerCategoria {

    fun insertar(categoria: Categoria): Long {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", categoria.nombre)
            put("descripcion", categoria.descripcion)
            put("estado_sync", 0)
            put("id_api", categoria.idApi)
        }
        val resultado = db.insert("tb_categoria", null, valores)
        db.close()
        return resultado
    }

    fun listar(): ArrayList<Categoria> {
        val lista = ArrayList<Categoria>()
        val db = AppConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_categoria WHERE estado_sync != 2", null)
        while (cursor.moveToNext()) {
            lista.add(Categoria(
                cod = cursor.getInt(0),
                nombre = cursor.getString(1),
                descripcion = cursor.getString(2) ?: "",
                estadoSync = cursor.getInt(3),
                idApi = cursor.getInt(4)
            ))
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizar(categoria: Categoria): Int {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", categoria.nombre)
            put("descripcion", categoria.descripcion)
            put("estado_sync", 0)
        }
        val resultado = db.update("tb_categoria", valores, "cod=?", arrayOf(categoria.cod.toString()))
        db.close()
        return resultado
    }

    fun eliminar(cod: Int): Int {
        val db = AppConfig.BD.writableDatabase
        val resultado = db.delete("tb_categoria", "cod=?", arrayOf(cod.toString()))
        db.close()
        return resultado
    }

    fun buscarPorIdApi(idApi: Int): Categoria? {
        val db = AppConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_categoria WHERE id_api = ?", arrayOf(idApi.toString()))
        var categoria: Categoria? = null
        if (cursor.moveToFirst()) {
            categoria = Categoria(
                cod = cursor.getInt(0),
                nombre = cursor.getString(1),
                descripcion = cursor.getString(2) ?: "",
                estadoSync = cursor.getInt(3),
                idApi = cursor.getInt(4)
            )
        }
        cursor.close()
        db.close()
        return categoria
    }

    fun insertarDesdeApi(categoria: Categoria): Long {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", categoria.nombre)
            put("descripcion", categoria.descripcion)
            put("estado_sync", 1)
            put("id_api", categoria.idApi)
        }
        val resultado = db.insert("tb_categoria", null, valores)
        db.close()
        return resultado
    }

    fun actualizarDesdeApi(categoria: Categoria): Int {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", categoria.nombre)
            put("descripcion", categoria.descripcion)
            put("estado_sync", 1)
        }
        val resultado = db.update("tb_categoria", valores, "id_api=?", arrayOf(categoria.idApi.toString()))
        db.close()
        return resultado
    }

    fun listarPendientes(): ArrayList<Categoria> {
        val lista = ArrayList<Categoria>()
        val db = AppConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_categoria WHERE estado_sync = 0", null)
        while (cursor.moveToNext()) {
            lista.add(Categoria(
                cod = cursor.getInt(0),
                nombre = cursor.getString(1),
                descripcion = cursor.getString(2) ?: "",
                estadoSync = cursor.getInt(3),
                idApi = cursor.getInt(4)
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
        val resultado = db.update("tb_categoria", valores, "cod=?", arrayOf(cod.toString()))
        db.close()
        return resultado
    }

    fun marcarParaBorrar(cod: Int, idApi: Int): Int {
        val db = AppConfig.BD.writableDatabase
        return if (idApi == 0) {
            val resultado = db.delete("tb_categoria", "cod=?", arrayOf(cod.toString()))
            db.close()
            resultado
        } else {
            val valores = ContentValues().apply { put("estado_sync", 2) }
            val resultado = db.update("tb_categoria", valores, "cod=?", arrayOf(cod.toString()))
            db.close()
            resultado
        }
    }

    fun listarPendientesBorrar(): ArrayList<Categoria> {
        val lista = ArrayList<Categoria>()
        val db = AppConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_categoria WHERE estado_sync = 2", null)
        while (cursor.moveToNext()) {
            lista.add(Categoria(
                cod = cursor.getInt(0),
                nombre = cursor.getString(1),
                descripcion = cursor.getString(2) ?: "",
                estadoSync = cursor.getInt(3),
                idApi = cursor.getInt(4)
            ))
        }
        cursor.close()
        db.close()
        return lista
    }
}
