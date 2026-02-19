package pe.com.cibertec.inventarioferreteriazamora.data

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pe.com.cibertec.inventarioferreteriazamora.utils.AppConfig

class InitBD : SQLiteOpenHelper(
    AppConfig.CONTEXTO,
    "inventarioZamora.bd", null, 2
) {

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = """
            CREATE TABLE tb_producto (
                cod INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                categoria TEXT,
                precio REAL NOT NULL,
                stock INTEGER NOT NULL,
                estado_sync INTEGER DEFAULT 0,
                id_api INTEGER DEFAULT 0
            )
        """.trimIndent()
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE tb_producto ADD COLUMN id_api INTEGER DEFAULT 0")
        }
    }
}
