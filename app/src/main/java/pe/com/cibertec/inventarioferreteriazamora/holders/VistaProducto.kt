package pe.com.cibertec.inventarioferreteriazamora.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import pe.com.cibertec.inventarioferreteriazamora.R

class VistaProducto(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
    val tvCategoria: TextView = itemView.findViewById(R.id.tvCategoria)
    val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
    val tvStock: TextView = itemView.findViewById(R.id.tvStock)
    val tvSync: TextView = itemView.findViewById(R.id.tvSync)
    val btnEditar: MaterialButton = itemView.findViewById(R.id.btnEditar)
    val btnEliminar: MaterialButton = itemView.findViewById(R.id.btnEliminar)
}
