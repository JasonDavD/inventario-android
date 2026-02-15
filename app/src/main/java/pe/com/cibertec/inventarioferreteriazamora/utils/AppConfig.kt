package pe.com.cibertec.inventarioferreteriazamora.utils

import android.app.Application
import android.content.Context
import com.example.appproyecto.data.InitBD

class AppConfig: Application() {
    //declarar vaiables globales
    companion object{
        lateinit var CONTEXTO: Context
        lateinit var BD: InitBD

    }
    //inicializar las variables globales
    override fun onCreate() {
        CONTEXTO=applicationContext
        BD= InitBD()
        super.onCreate()
    }

}