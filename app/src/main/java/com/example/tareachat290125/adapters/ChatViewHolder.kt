package com.example.tareachat290125.adapters

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tareachat290125.R
import com.example.tareachat290125.databinding.ChatLayoutBinding
import com.example.tareachat290125.models.ChatModel
import java.text.SimpleDateFormat
import java.util.Date

class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ChatLayoutBinding.bind(view)

    fun render(item: ChatModel, emailUsuarioLogueado: String, onDeleteClickListener: (String) -> Unit) {
        val params = binding.cvChat.layoutParams as FrameLayout.LayoutParams

        if (item.email == emailUsuarioLogueado) {
            binding.cvChat.setCardBackgroundColor(binding.tvFecha.context.getColor(R.color.color_logeado))
            params.gravity = Gravity.END
            binding.ivEliminar.visibility = View.VISIBLE
            binding.ivEliminar.setOnClickListener {
                onDeleteClickListener(item.fecha.toString())
            }
        } else {
            binding.cvChat.setCardBackgroundColor(binding.tvFecha.context.getColor(R.color.color_normal))
            params.gravity = Gravity.START
            binding.ivEliminar.visibility = View.GONE
        }

        binding.tvEmail.text = item.email
        binding.tvMensaje.text = item.mensaje
        binding.tvFecha.text = fechaFormateada(item.fecha)
    }

    private fun fechaFormateada(fecha: Long): String {
        val date = Date(fecha)
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm a")
        return format.format(date)
    }
}