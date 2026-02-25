package pe.com.cibertec.inventarioferreteriazamora.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import pe.com.cibertec.inventarioferreteriazamora.R

class VistaCategoria(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvNombreCategoria: TextView = itemView.findViewById(R.id.tvNombreCategoria)
    val tvDescripcionCategoria: TextView = itemView.findViewById(R.id.tvDescripcionCategoria)
    val tvSyncCategoria: TextView = itemView.findViewById(R.id.tvSyncCategoria)
    val btnEditarCategoria: MaterialButton = itemView.findViewById(R.id.btnEditarCategoria)
    val btnEliminarCategoria: MaterialButton = itemView.findViewById(R.id.btnEliminarCategoria)
}
