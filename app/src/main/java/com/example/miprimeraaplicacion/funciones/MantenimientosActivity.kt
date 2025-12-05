package com.example.miprimeraaplicacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.miprimeraaplicacion.models.Mantenimiento
import com.example.miprimeraaplicacion.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MantenimientosActivity : AppCompatActivity() {

    private lateinit var lista: ListView
    private lateinit var edtNombre: EditText
    private lateinit var edtTipo: EditText
    private lateinit var edtFecha: EditText
    private lateinit var btnCrear: Button

    private var listaDatos = mutableListOf<Mantenimiento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mantenimientos)

        lista = findViewById(R.id.lista_mantenimientos)
        edtNombre = findViewById(R.id.edt_nombre)
        edtTipo = findViewById(R.id.edt_tipo)
        edtFecha = findViewById(R.id.edt_fecha)
        btnCrear = findViewById(R.id.btn_crear)

        cargarMantenimientos()

        btnCrear.setOnClickListener {
            crearMantenimiento()
        }

        lista.setOnItemClickListener { _, _, position, _ ->
            val item = listaDatos[position]
            mostrarOpciones(item)
        }
    }

    // ------------------- LISTAR -------------------
    private fun cargarMantenimientos() {
        RetrofitClient.instance.getMantenimientos()
            .enqueue(object : Callback<List<Mantenimiento>> {
                override fun onResponse(
                    call: Call<List<Mantenimiento>>,
                    response: Response<List<Mantenimiento>>
                ) {
                    if (response.isSuccessful) {
                        listaDatos = response.body()?.toMutableList() ?: mutableListOf()
                        val nombres = listaDatos.map { "${it.nombre} - ${it.tipo} - ${it.fecha}" }
                        lista.adapter =
                            ArrayAdapter(this@MantenimientosActivity, android.R.layout.simple_list_item_1, nombres)
                    }
                }

                override fun onFailure(call: Call<List<Mantenimiento>>, t: Throwable) {
                    Toast.makeText(this@MantenimientosActivity, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // ------------------- CREAR -------------------
    private fun crearMantenimiento() {
        val mantenimiento = Mantenimiento(
            nombre = edtNombre.text.toString(),
            tipo = edtTipo.text.toString(),
            fecha = edtFecha.text.toString()
        )

        RetrofitClient.instance.createMantenimiento(mantenimiento)
            .enqueue(object : Callback<Mantenimiento> {
                override fun onResponse(
                    call: Call<Mantenimiento>,
                    response: Response<Mantenimiento>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MantenimientosActivity, "Creado", Toast.LENGTH_SHORT).show()
                        cargarMantenimientos()
                        limpiarCampos()
                    }
                }

                override fun onFailure(call: Call<Mantenimiento>, t: Throwable) {
                    Toast.makeText(this@MantenimientosActivity, "Error al crear", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun limpiarCampos() {
        edtNombre.text.clear()
        edtTipo.text.clear()
        edtFecha.text.clear()
    }

    // ------------------- POPUP EDITAR / ELIMINAR -------------------
    private fun mostrarOpciones(item: Mantenimiento) {
        val opciones = arrayOf("Editar", "Eliminar")

        AlertDialog.Builder(this)
            .setTitle("Acciones")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> editar(item)
                    1 -> eliminar(item)
                }
            }
            .show()
    }

    // ------------------- EDITAR -------------------
    private fun editar(item: Mantenimiento) {
        val nuevoNombre = edtNombre.text.toString()
        val nuevoTipo = edtTipo.text.toString()
        val nuevaFecha = edtFecha.text.toString()

        val actualizado = Mantenimiento(
            id = item.id,
            nombre = nuevoNombre.ifEmpty { item.nombre },
            tipo = nuevoTipo.ifEmpty { item.tipo },
            fecha = nuevaFecha.ifEmpty { item.fecha }
        )

        RetrofitClient.instance.updateMantenimiento(item.id!!, actualizado)
            .enqueue(object : Callback<Mantenimiento> {
                override fun onResponse(call: Call<Mantenimiento>, response: Response<Mantenimiento>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MantenimientosActivity, "Actualizado", Toast.LENGTH_SHORT).show()
                        cargarMantenimientos()
                        limpiarCampos()
                    }
                }

                override fun onFailure(call: Call<Mantenimiento>, t: Throwable) {
                    Toast.makeText(this@MantenimientosActivity, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // ------------------- ELIMINAR -------------------
    private fun eliminar(item: Mantenimiento) {
        RetrofitClient.instance.deleteMantenimiento(item.id!!)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MantenimientosActivity, "Eliminado", Toast.LENGTH_SHORT).show()
                        cargarMantenimientos()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@MantenimientosActivity, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
