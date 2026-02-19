package pe.com.cibertec.inventarioferreteriazamora.controller

import android.content.ContentValues
import pe.com.cibertec.inventarioferreteriazamora.modelos.Producto
import pe.com.cibertec.inventarioferreteriazamora.utils.AppConfig

class ControllerProducto {

    fun insertar(producto: Producto): Long {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("categoria", producto.categoria)
            put("precio", producto.precio)
            put("stock", producto.stock)
            put("estado_sync", 0)
            put("id_api", producto.idApi)
        }
        val resultado = db.insert("tb_producto", null, valores)
        db.close()
        return resultado
    }

    fun listar(): ArrayList<Producto> {
        val lista = ArrayList<Producto>()
        val db = AppConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_producto", null)
        while (cursor.moveToNext()) {
            val producto = Producto(
                cod = cursor.getInt(0),
                nombre = cursor.getString(1),
                categoria = cursor.getString(2),
                precio = cursor.getDouble(3),
                stock = cursor.getInt(4),
                estadoSync = cursor.getInt(5),
                idApi = cursor.getInt(6)
            )
            lista.add(producto)
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizar(producto: Producto): Int {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("categoria", producto.categoria)
            put("precio", producto.precio)
            put("stock", producto.stock)
            put("estado_sync", 0)
            put("id_api", producto.idApi)
        }
        val resultado = db.update("tb_producto", valores, "cod=?", arrayOf(producto.cod.toString()))
        db.close()
        return resultado
    }

    fun eliminar(cod: Int): Int {
        val db = AppConfig.BD.writableDatabase
        val resultado = db.delete("tb_producto", "cod=?", arrayOf(cod.toString()))
        db.close()
        return resultado
    }

    fun listarPendientes(): ArrayList<Producto> {
        val lista = ArrayList<Producto>()
        val db = AppConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_producto WHERE estado_sync = 0", null)
        while (cursor.moveToNext()) {
            val producto = Producto(
                cod = cursor.getInt(0),
                nombre = cursor.getString(1),
                categoria = cursor.getString(2),
                precio = cursor.getDouble(3),
                stock = cursor.getInt(4),
                estadoSync = cursor.getInt(5),
                idApi = cursor.getInt(6)
            )
            lista.add(producto)
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
        val resultado = db.update("tb_producto", valores, "cod=?", arrayOf(cod.toString()))
        db.close()
        return resultado
    }

    fun buscarPorIdApi(idApi: Int): Producto? {
        val db = AppConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_producto WHERE id_api = ?", arrayOf(idApi.toString()))
        var producto: Producto? = null
        if (cursor.moveToFirst()) {
            producto = Producto(
                cod = cursor.getInt(0),
                nombre = cursor.getString(1),
                categoria = cursor.getString(2),
                precio = cursor.getDouble(3),
                stock = cursor.getInt(4),
                estadoSync = cursor.getInt(5),
                idApi = cursor.getInt(6)
            )
        }
        cursor.close()
        db.close()
        return producto
    }

    fun insertarDesdeApi(producto: Producto): Long {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("categoria", producto.categoria)
            put("precio", producto.precio)
            put("stock", producto.stock)
            put("estado_sync", 1)
            put("id_api", producto.idApi)
        }
        val resultado = db.insert("tb_producto", null, valores)
        db.close()
        return resultado
    }

    fun actualizarDesdeApi(producto: Producto): Int {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("categoria", producto.categoria)
            put("precio", producto.precio)
            put("stock", producto.stock)
            put("estado_sync", 1)
        }
        val resultado = db.update("tb_producto", valores, "id_api=?", arrayOf(producto.idApi.toString()))
        db.close()
        return resultado
    }
}
