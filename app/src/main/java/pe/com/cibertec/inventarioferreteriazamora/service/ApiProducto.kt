package pe.com.cibertec.inventarioferreteriazamora.service

import pe.com.cibertec.inventarioferreteriazamora.modelos.Producto
import retrofit2.Call
import retrofit2.http.*

interface ApiProducto {

    @GET("api/productos")
    fun listarProductos(): Call<List<Producto>>

    @POST("api/productos")
    fun crearProducto(@Body producto: Producto): Call<Producto>

    @PUT("api/productos/{id}")
    fun actualizarProducto(@Path("id") id: Int, @Body producto: Producto): Call<Producto>

    @DELETE("api/productos/{id}")
    fun eliminarProducto(@Path("id") id: Int): Call<Void>
}
