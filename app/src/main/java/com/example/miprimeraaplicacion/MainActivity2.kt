package com.example.miprimeraaplicacion

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {

    private val REQUEST_CAMERA = 100
    private val PERMISO_CAMARA = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        val txUsuario: TextView = findViewById(R.id.id_usuario_logueado)
        val recibirUsername = intent.getStringExtra("par_usern")
        txUsuario.text = recibirUsername.toString()

        val spinner = findViewById<Spinner>(R.id.id_spinner)
        val opciones = listOf("Manteciones", "Contacto", "Ayuda")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                position: Int,
                id: Long
            ) {
                val selectedOpciones = opciones[position]
                Toast.makeText(this@MainActivity2, selectedOpciones, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        // BOTÓN DE LA CÁMARA
        val btnCamara: Button = findViewById(R.id.btn_camara)

        btnCamara.setOnClickListener {
            verificarPermisoYabrirCamara()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // 🔒 Primero verifica permisos
    private fun verificarPermisoYabrirCamara() {
        val permiso = android.Manifest.permission.CAMERA

        if (ContextCompat.checkSelfPermission(this, permiso)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permiso),
                PERMISO_CAMARA
            )
        } else {
            abrirCamara()
        }
    }

    // 📸 Abre la cámara
    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    // 🔙 Resultado del permiso
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISO_CAMARA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara()
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 📷 Resultado de la foto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Foto tomada correctamente", Toast.LENGTH_SHORT).show()
        }
    }
}
