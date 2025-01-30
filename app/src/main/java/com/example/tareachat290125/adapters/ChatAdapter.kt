package com.example.tareachat290125.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tareachat290125.R
import com.example.tareachat290125.models.ChatModel

class ChatAdapter(
    var lista: MutableList<ChatModel>,
    private val emailUsuarioLogueado: String,
    private val onDeleteClickListener: (String) -> Unit
) : RecyclerView.Adapter<ChatViewHolder>() {

    fun actualizarLista(nuevaLista: MutableList<ChatModel>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_layout, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ChatViewHolder,
        position: Int
    ) {
        holder.render(lista[position], emailUsuarioLogueado) { messageId ->
            onDeleteClickListener(
                messageId
            )
        }
    }

    override fun getItemCount() = lista.size
}