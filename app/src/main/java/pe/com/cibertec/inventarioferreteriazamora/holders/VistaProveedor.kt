package pe.com.cibertec.inventarioferreteriazamora.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import pe.com.cibertec.inventarioferreteriazamora.R

class VistaProveedor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvNombreProveedor: TextView = itemView.findViewById(R.id.tvNombreProveedor)
    val tvTelefonoProveedor: TextView = itemView.findViewById(R.id.tvTelefonoProveedor)
    val tvDireccionProveedor: TextView = itemView.findViewById(R.id.tvDireccionProveedor)
    val tvSyncProveedor: TextView = itemView.findViewById(R.id.tvSyncProveedor)
    val btnEditarProveedor: MaterialButton = itemView.findViewById(R.id.btnEditarProveedor)
    val btnEliminarProveedor: MaterialButton = itemView.findViewById(R.id.btnEliminarProveedor)
}
