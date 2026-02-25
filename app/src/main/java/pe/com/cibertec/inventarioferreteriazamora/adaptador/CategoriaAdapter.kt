package pe.com.cibertec.inventarioferreteriazamora.adaptador

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.holders.VistaCategoria
import pe.com.cibertec.inventarioferreteriazamora.modelos.Categoria

class CategoriaAdapter(
    private var listaCategorias: ArrayList<Categoria>,
    private val onEditar: (Categoria) -> Unit,
    private val onEliminar: (Categoria) -> Unit
) : RecyclerView.Adapter<VistaCategoria>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaCategoria {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return VistaCategoria(vista)
    }

    override fun onBindViewHolder(holder: VistaCategoria, position: Int) {
        val categoria = listaCategorias[position]

        holder.tvNombreCategoria.text = categoria.nombre
        holder.tvDescripcionCategoria.text = if (categoria.descripcion.isNotEmpty()) categoria.descripcion else "Sin descripcion"

        if (categoria.estadoSync == 1) {
            holder.tvSyncCategoria.text = "Sincronizado"
            holder.tvSyncCategoria.setBackgroundColor(Color.parseColor("#4CAF50"))
        } else {
            holder.tvSyncCategoria.text = "Pendiente"
            holder.tvSyncCategoria.setBackgroundColor(Color.parseColor("#FF9800"))
        }

        holder.btnEditarCategoria.setOnClickListener { onEditar(categoria) }
        holder.btnEliminarCategoria.setOnClickListener { onEliminar(categoria) }
    }

    override fun getItemCount(): Int = listaCategorias.size

    fun actualizarLista(nuevaLista: ArrayList<Categoria>) {
        listaCategorias = nuevaLista
        notifyDataSetChanged()
    }
}
