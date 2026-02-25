package pe.com.cibertec.inventarioferreteriazamora.adaptador

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.com.cibertec.inventarioferreteriazamora.R
import pe.com.cibertec.inventarioferreteriazamora.holders.VistaProveedor
import pe.com.cibertec.inventarioferreteriazamora.modelos.Proveedor

class ProveedorAdapter(
    private var listaProveedores: ArrayList<Proveedor>,
    private val onEditar: (Proveedor) -> Unit,
    private val onEliminar: (Proveedor) -> Unit
) : RecyclerView.Adapter<VistaProveedor>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaProveedor {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_proveedor, parent, false)
        return VistaProveedor(vista)
    }

    override fun onBindViewHolder(holder: VistaProveedor, position: Int) {
        val proveedor = listaProveedores[position]

        holder.tvNombreProveedor.text = proveedor.nombre
        holder.tvTelefonoProveedor.text = if (proveedor.telefono.isNotEmpty()) proveedor.telefono else "Sin telefono"
        holder.tvDireccionProveedor.text = if (proveedor.direccion.isNotEmpty()) proveedor.direccion else "Sin direccion"

        if (proveedor.estadoSync == 1) {
            holder.tvSyncProveedor.text = "Sincronizado"
            holder.tvSyncProveedor.setBackgroundColor(Color.parseColor("#4CAF50"))
        } else {
            holder.tvSyncProveedor.text = "Pendiente"
            holder.tvSyncProveedor.setBackgroundColor(Color.parseColor("#FF9800"))
        }

        holder.btnEditarProveedor.setOnClickListener { onEditar(proveedor) }
        holder.btnEliminarProveedor.setOnClickListener { onEliminar(proveedor) }
    }

    override fun getItemCount(): Int = listaProveedores.size

    fun actualizarLista(nuevaLista: ArrayList<Proveedor>) {
        listaProveedores = nuevaLista
        notifyDataSetChanged()
    }
}
