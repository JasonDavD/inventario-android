package pe.com.cibertec.inventarioferreteriazamora.utils

import pe.com.cibertec.inventarioferreteriazamora.service.ApiProducto

class ApiUtils {

    companion object {
        fun getAPIProducto(): ApiProducto {
            return RetrofitClient.getClient(AppConfig.BASE_URL)
                .create(ApiProducto::class.java)
        }
    }
}
