package com.example.tareachat290125

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tareachat290125.adapters.ChatAdapter
import com.example.tareachat290125.databinding.ActivityChatBinding
import com.example.tareachat290125.models.ChatModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlin.toString

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    var emailUsuarioLogeado = ""

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    private var listaChats = mutableListOf<ChatModel>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        emailUsuarioLogeado = auth.currentUser?.email.toString()

        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.setPersistenceEnabled(true)

        databaseRef = FirebaseDatabase.getInstance().getReference("chat")

        setRecycler()
        setListeners()
    }

    //----------------------------------------------------------------------------------------------
    private fun setRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvChat.layoutManager = layoutManager

        adapter = ChatAdapter(listaChats, emailUsuarioLogeado) { messageId -> eliminarMensaje(messageId) }
        binding.rvChat.adapter = adapter
    }

    //----------------------------------------------------------------------------------------------
    private fun eliminarMensaje(messageId: String) {
        databaseRef.child(messageId).removeValue()
    }

    //----------------------------------------------------------------------------------------------
    private fun setListeners() {
        binding.btnEnviar.setOnClickListener {
            enviarMensaje()
        }

        //Ponemos un listener a la base de datos para que se actualice la lista de chats
        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                listaChats.clear()
                for (nodo in snapshot.children) {
                    val chatNodo = nodo.getValue(ChatModel::class.java)
                    if (chatNodo != null) {
                        listaChats.add(chatNodo)
                    }
                }
                listaChats.sortBy { it.fecha }
                //adapter.lista = listaChats
                //adapter.notifyDataSetChanged()
                adapter.actualizarLista(listaChats)

                // Scroll al último mensaje
                binding.rvChat.scrollToPosition(listaChats.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ChatActivity,
                    "Error al recuperar los chats",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        //Listener para menú lateral
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_salir -> {
                    finishAffinity()
                    true
                }

                R.id.item_logout -> {
                    auth.signOut()
                    finish()
                    true
                }

                R.id.item_borrar -> {
                    borrarChatUsuario()
                    true
                }

                else -> false
            }
        }

        // Listener para cuando le de al intro
        binding.etMensaje.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                enviarMensaje()
                ocultarTeclado()
                true
            } else {
                false
            }
        }
    }

    private fun borrarChatUsuario() {
        databaseRef.get().addOnSuccessListener {
            for (nodo in it.children) {
                val correo = nodo.child("email").getValue(String::class.java)
                if (correo == emailUsuarioLogeado) {
                    nodo.ref.removeValue()
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    private fun enviarMensaje() {
        val mensaje = binding.etMensaje.text.toString().trim()
        if (mensaje.isEmpty()) return
        val fecha = System.currentTimeMillis()
        val chat = ChatModel(emailUsuarioLogeado, mensaje, fecha)
        val key = fecha.toString()
        databaseRef.child(key).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseRef.child(key).setValue(chat)
                    .addOnSuccessListener {
                        binding.etMensaje.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@ChatActivity,
                            "No se pudo enviar el mensaje",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    //----------------------------------------------------------------------------------------------
    private fun ocultarTeclado() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}