package pe.com.cibertec.inventarioferreteriazamora.data

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pe.com.cibertec.inventarioferreteriazamora.utils.AppConfig

class InitBD : SQLiteOpenHelper(
    AppConfig.CONTEXTO,
    "inventarioZamora.bd",null,1) {

    override fun onCreate(db: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        TODO("Not yet implemented")
    }

}