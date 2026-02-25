package pe.com.cibertec.inventarioferreteriazamora.data

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pe.com.cibertec.inventarioferreteriazamora.utils.AppConfig

class InitBD : SQLiteOpenHelper(
    AppConfig.CONTEXTO,
    "inventarioZamora.bd", null, 3
) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE tb_producto (
                cod INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                categoria TEXT,
                precio REAL NOT NULL,
                stock INTEGER NOT NULL,
                estado_sync INTEGER DEFAULT 0,
                id_api INTEGER DEFAULT 0,
                categoria_id INTEGER DEFAULT 0,
                categoria_nombre TEXT DEFAULT '',
                proveedor_id INTEGER DEFAULT 0,
                proveedor_nombre TEXT DEFAULT ''
            )
        """.trimIndent())

        db?.execSQL("""
            CREATE TABLE tb_categoria (
                cod INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                descripcion TEXT,
                estado_sync INTEGER DEFAULT 0,
                id_api INTEGER DEFAULT 0
            )
        """.trimIndent())

        db?.execSQL("""
            CREATE TABLE tb_proveedor (
                cod INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                telefono TEXT,
                direccion TEXT,
                estado_sync INTEGER DEFAULT 0,
                id_api INTEGER DEFAULT 0
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE tb_producto ADD COLUMN id_api INTEGER DEFAULT 0")
        }
        if (oldVersion < 3) {
            db?.execSQL("ALTER TABLE tb_producto ADD COLUMN categoria_id INTEGER DEFAULT 0")
            db?.execSQL("ALTER TABLE tb_producto ADD COLUMN categoria_nombre TEXT DEFAULT ''")
            db?.execSQL("ALTER TABLE tb_producto ADD COLUMN proveedor_id INTEGER DEFAULT 0")
            db?.execSQL("ALTER TABLE tb_producto ADD COLUMN proveedor_nombre TEXT DEFAULT ''")
            db?.execSQL("""
                CREATE TABLE IF NOT EXISTS tb_categoria (
                    cod INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    descripcion TEXT,
                    estado_sync INTEGER DEFAULT 0,
                    id_api INTEGER DEFAULT 0
                )
            """.trimIndent())
            db?.execSQL("""
                CREATE TABLE IF NOT EXISTS tb_proveedor (
                    cod INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    telefono TEXT,
                    direccion TEXT,
                    estado_sync INTEGER DEFAULT 0,
                    id_api INTEGER DEFAULT 0
                )
            """.trimIndent())
        }
    }
}
