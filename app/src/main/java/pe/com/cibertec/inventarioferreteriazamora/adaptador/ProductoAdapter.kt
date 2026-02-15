package pe.com.cibertec.inventarioferreteriazamora.adaptador

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.holders.VistaProducto
import pe.com.cibertec.inventarioferreteriazamora.modelos.Producto

class ProductoAdapter(
    private var listaProductos: ArrayList<Producto>,
    private val onEditar: (Producto) -> Unit,
    private val onEliminar: (Producto) -> Unit
) : RecyclerView.Adapter<VistaProducto>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaProducto {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return VistaProducto(vista)
    }

    override fun onBindViewHolder(holder: VistaProducto, position: Int) {
        val producto = listaProductos[position]

        holder.tvNombre.text = producto.nombre
        holder.tvCategoria.text = producto.categoria
        holder.tvPrecio.text = "S/ %.2f".format(producto.precio)
        holder.tvStock.text = "Stock: ${producto.stock}"

        if (producto.estadoSync == 1) {
            holder.tvSync.text = "Sincronizado"
            holder.tvSync.setBackgroundColor(Color.parseColor("#4CAF50"))
        } else {
            holder.tvSync.text = "Pendiente"
            holder.tvSync.setBackgroundColor(Color.parseColor("#FF9800"))
        }

        holder.btnEditar.setOnClickListener {
            onEditar(producto)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminar(producto)
        }
    }

    override fun getItemCount(): Int = listaProductos.size

    fun actualizarLista(nuevaLista: ArrayList<Producto>) {
        listaProductos = nuevaLista
        notifyDataSetChanged()
    }
}
