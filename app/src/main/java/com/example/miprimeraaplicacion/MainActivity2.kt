package com.example.miprimeraaplicacion

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.*

class MainActivity2 : AppCompatActivity() {

    private val REQUEST_CAMERA = 100
    private val PERMISO_CAMARA = 200
    private val PERMISO_GPS = 300

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var txtUbicacion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        val txUsuario: TextView = findViewById(R.id.id_usuario_logueado)
        val recibirUsername = intent.getStringExtra("par_usern")
        txUsuario.text = recibirUsername.toString()

        // TEXTO DONDE SE MOSTRARÁ LA UBICACIÓN
        txtUbicacion = findViewById(R.id.id_txt_ubicacion)

        // Inicializar GPS
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // ------------------ SPINNER ------------------
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

        // ------------------ BOTÓN CÁMARA ------------------
        val btnCamara: Button = findViewById(R.id.btn_camara)
        btnCamara.setOnClickListener {
            verificarPermisoYabrirCamara()
        }

        val btnMantenimientos = findViewById<Button>(R.id.btn_mantenimientos)

        btnMantenimientos.setOnClickListener {
            val intent = Intent(this, MantenimientosActivity::class.java)
            startActivity(intent)
        }


        // ------------------ BOTÓN OBTENER UBICACIÓN ------------------
        val btnGps: Button = findViewById(R.id.btn_gps)
        btnGps.setOnClickListener {
            verificarPermisoGPS()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // ------------------ PERMISO GPS ------------------
    private fun verificarPermisoGPS() {
        val permiso = Manifest.permission.ACCESS_FINE_LOCATION

        if (ContextCompat.checkSelfPermission(this, permiso)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permiso),
                PERMISO_GPS
            )
        } else {
            obtenerUbicacion()
        }
    }


    // ------------------ OBTENER UBICACIÓN ------------------
    @android.annotation.SuppressLint("MissingPermission")
    private fun obtenerUbicacion() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude

                txtUbicacion.text = "Lat: $lat\nLon: $lon"
            } else {
                txtUbicacion.text = "No se pudo obtener la ubicación"
            }
        }
    }

    // ------------------ PERMISOS RESULTADO ------------------
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

        if (requestCode == PERMISO_GPS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion()
            } else {
                Toast.makeText(this, "Permiso de GPS denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ------------------ CÁMARA ------------------
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

    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Foto tomada correctamente", Toast.LENGTH_SHORT).show()
        }
    }
}
