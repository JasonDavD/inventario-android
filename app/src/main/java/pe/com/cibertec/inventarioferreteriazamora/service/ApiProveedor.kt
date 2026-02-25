package pe.com.cibertec.inventarioferreteriazamora.service

import pe.com.cibertec.inventarioferreteriazamora.modelos.Proveedor
import retrofit2.Call
import retrofit2.http.*

interface ApiProveedor {

    @GET("api/proveedores")
    fun listarProveedores(): Call<List<Proveedor>>

    @POST("api/proveedores")
    fun crearProveedor(@Body proveedor: Proveedor): Call<Proveedor>

    @PUT("api/proveedores/{id}")
    fun actualizarProveedor(@Path("id") id: Int, @Body proveedor: Proveedor): Call<Proveedor>

    @DELETE("api/proveedores/{id}")
    fun eliminarProveedor(@Path("id") id: Int): Call<Void>
}
