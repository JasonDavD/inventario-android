package pe.com.cibertec.inventarioferreteriazamora.service

import pe.com.cibertec.inventarioferreteriazamora.modelos.Categoria
import retrofit2.Call
import retrofit2.http.*

interface ApiCategoria {

    @GET("api/categorias")
    fun listarCategorias(): Call<List<Categoria>>

    @POST("api/categorias")
    fun crearCategoria(@Body categoria: Categoria): Call<Categoria>

    @PUT("api/categorias/{id}")
    fun actualizarCategoria(@Path("id") id: Int, @Body categoria: Categoria): Call<Categoria>

    @DELETE("api/categorias/{id}")
    fun eliminarCategoria(@Path("id") id: Int): Call<Void>
}
