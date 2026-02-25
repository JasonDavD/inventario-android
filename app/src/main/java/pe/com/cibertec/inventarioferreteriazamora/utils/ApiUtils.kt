package pe.com.cibertec.inventarioferreteriazamora.utils

import pe.com.cibertec.inventarioferreteriazamora.service.ApiCategoria
import pe.com.cibertec.inventarioferreteriazamora.service.ApiProducto
import pe.com.cibertec.inventarioferreteriazamora.service.ApiProveedor

class ApiUtils {

    companion object {
        fun getAPIProducto(): ApiProducto {
            return RetrofitClient.getClient(AppConfig.BASE_URL)
                .create(ApiProducto::class.java)
        }

        fun getAPICategoria(): ApiCategoria {
            return RetrofitClient.getClient(AppConfig.BASE_URL)
                .create(ApiCategoria::class.java)
        }

        fun getAPIProveedor(): ApiProveedor {
            return RetrofitClient.getClient(AppConfig.BASE_URL)
                .create(ApiProveedor::class.java)
        }
    }
}
