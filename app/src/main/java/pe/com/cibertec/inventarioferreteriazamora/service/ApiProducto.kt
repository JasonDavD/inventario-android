package pe.com.cibertec.inventarioferreteriazamora.service

import pe.com.cibertec.inventarioferreteriazamora.modelos.Producto
import pe.com.cibertec.inventarioferreteriazamora.modelos.ProductoRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiProducto {

    @GET("api/productos")
    fun listarProductos(): Call<List<Producto>>

    @POST("api/productos")
    fun crearProducto(@Body request: ProductoRequest): Call<Producto>

    @PUT("api/productos/{id}")
    fun actualizarProducto(@Path("id") id: Int, @Body request: ProductoRequest): Call<Producto>

    @DELETE("api/productos/{id}")
    fun eliminarProducto(@Path("id") id: Int): Call<Void>
}
