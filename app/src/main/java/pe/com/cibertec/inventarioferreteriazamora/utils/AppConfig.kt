package pe.com.cibertec.inventarioferreteriazamora.utils

import android.app.Application
import android.content.Context
import pe.com.cibertec.inventarioferreteriazamora.data.InitBD

class AppConfig : Application() {

    companion object {
        lateinit var CONTEXTO: Context
        lateinit var BD: InitBD
        const val BASE_URL = "https://tu-app.onrender.com/"
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXTO = applicationContext
        BD = InitBD()
    }
}
