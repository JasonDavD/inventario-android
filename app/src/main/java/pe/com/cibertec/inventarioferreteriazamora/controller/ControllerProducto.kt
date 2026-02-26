package pe.com.cibertec.inventarioferreteriazamora.controller

import android.content.ContentValues
import pe.com.cibertec.inventarioferreteriazamora.modelos.Producto
import pe.com.cibertec.inventarioferreteriazamora.utils.AppConfig

class ControllerProducto {

    fun insertar(producto: Producto): Long {
        val db = AppConfig.BD.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("categoria", "")
            put("precio", producto.precio)
            put("stock", producto.stock)
            put("estado_sync", 0)
            put("id_api", producto.idApi)
            put("categoria_id", producto.categoriaId)
            put("categoria_nombre", producto.categoriaNombre)
            put("proveedor_id", producto.proveedorId)
            put("proveedor_nombre", producto.proveedorNombre)
        }
        val resultado = db.insert("tb_producto", null, valores)
        db.close()
        return resultado
    }

    fun listar(): ArrayList<Producto> {
        val lista = ArrayList<Producto>()
        val db = AppConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_producto WHERE estado_sync != 2", null)
        while (cursor.moveToNext()) {
            val producto = Producto(
                cod = cursor.getInt(0),
                nombre = cursor.getString(1),
                precio = cursor.getDouble(3),
                stock = cursor.getInt(4),
                estadoSync = cursor.getInt(5),
                idApi = cursor.getInt(6),
                categoriaId = cursor.getInt(7),
                categoriaNombre = cursor.getString(8) ?: "",
                proveedorId = cursor.getInt(9),
                proveedorNombre = cursor.getString(10) ?: ""
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
            put("precio", producto.precio)
            put("stock", producto.stock)
            put("estado_sync", 0)
            put("id_api", producto.idApi)
            put("categoria_id", producto.categoriaId)
            put("categoria_nombre", producto.categoriaNombre)
            put("proveedor_id", producto.proveedorId)
            put("proveedor_nombre", producto.proveedorNombre)
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
                precio = cursor.getDouble(3),
                stock = cursor.getInt(4),
                estadoSync = cursor.getInt(5),
                idApi = cursor.getInt(6),
                categoriaId = cursor.getInt(7),
                categoriaNombre = cursor.getString(8) ?: "",
                proveedorId = cursor.getInt(9),
                proveedorNombre = cursor.getString(10) ?: ""
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
                precio = cursor.getDouble(3),
                stock = cursor.getInt(4),
                estadoSync = cursor.getInt(5),
                idApi = cursor.getInt(6),
                categoriaId = cursor.getInt(7),
                categoriaNombre = cursor.getString(8) ?: "",
                proveedorId = cursor.getInt(9),
                proveedorNombre = cursor.getString(10) ?: ""
            )
        }
        cursor.close()
        db.close()
        return producto
    }

    fun insertarDesdeApi(producto: Producto): Long {
        val db = AppConfig.BD.writableDatabase
        val catId = producto.categoriaApi?.id ?: 0
        val catNombre = producto.categoriaApi?.nombre ?: ""
        val provId = producto.proveedorApi?.id ?: 0
        val provNombre = producto.proveedorApi?.nombre ?: ""
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("categoria", "")
            put("precio", producto.precio)
            put("stock", producto.stock)
            put("estado_sync", 1)
            put("id_api", producto.idApi)
            put("categoria_id", catId)
            put("categoria_nombre", catNombre)
            put("proveedor_id", provId)
            put("proveedor_nombre", provNombre)
        }
        val resultado = db.insert("tb_producto", null, valores)
        db.close()
        return resultado
    }

    fun marcarParaBorrar(cod: Int, idApi: Int): Int {
        val db = AppConfig.BD.writableDatabase
        return if (idApi == 0) {
            val resultado = db.delete("tb_producto", "cod=?", arrayOf(cod.toString()))
            db.close()
            resultado
        } else {
            val valores = ContentValues().apply { put("estado_sync", 2) }
            val resultado = db.update("tb_producto", valores, "cod=?", arrayOf(cod.toString()))
            db.close()
            resultado
        }
    }

    fun listarPendientesBorrar(): ArrayList<Producto> {
        val lista = ArrayList<Producto>()
        val db = AppConfig.BD.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tb_producto WHERE estado_sync = 2", null)
        while (cursor.moveToNext()) {
            val producto = Producto(
                cod = cursor.getInt(0),
                nombre = cursor.getString(1),
                precio = cursor.getDouble(3),
                stock = cursor.getInt(4),
                estadoSync = cursor.getInt(5),
                idApi = cursor.getInt(6),
                categoriaId = cursor.getInt(7),
                categoriaNombre = cursor.getString(8) ?: "",
                proveedorId = cursor.getInt(9),
                proveedorNombre = cursor.getString(10) ?: ""
            )
            lista.add(producto)
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizarDesdeApi(producto: Producto): Int {
        val db = AppConfig.BD.writableDatabase
        val catId = producto.categoriaApi?.id ?: 0
        val catNombre = producto.categoriaApi?.nombre ?: ""
        val provId = producto.proveedorApi?.id ?: 0
        val provNombre = producto.proveedorApi?.nombre ?: ""
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("precio", producto.precio)
            put("stock", producto.stock)
            put("estado_sync", 1)
            put("categoria_id", catId)
            put("categoria_nombre", catNombre)
            put("proveedor_id", provId)
            put("proveedor_nombre", provNombre)
        }
        val resultado = db.update("tb_producto", valores, "id_api=?", arrayOf(producto.idApi.toString()))
        db.close()
        return resultado
    }
}
