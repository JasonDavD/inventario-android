package pe.com.cibertec.inventarioferreteriazamora.data

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pe.com.cibertec.inventarioferreteriazamora.utils.AppConfig

class InitBD : SQLiteOpenHelper(
    AppConfig.CONTEXTO,
    "inventarioZamora.bd", null, 1
) {

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = """
            CREATE TABLE tb_producto (
                cod INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                categoria TEXT,
                precio REAL NOT NULL,
                stock INTEGER NOT NULL,
                estado_sync INTEGER DEFAULT 0
            )
        """.trimIndent()
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS tb_producto")
        onCreate(db)
    }
}
