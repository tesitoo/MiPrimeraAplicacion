package com.example.miprimeraaplicacion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miprimeraaplicacion.funciones.OpMatematica

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)

        val eddigito1:EditText = findViewById(R.id.ed_digito1)
        val eddigito2:EditText= findViewById(R.id.ed_digito2)
        val txresultado:TextView = findViewById(R.id.tx_resultado)
        val btnresultado: Button = findViewById(R.id.btn_resultado)

        btnresultado.setOnClickListener{
            var numero1:Int = eddigito1.text.toString().toIntOrNull() ?: 0
            var numero2:Int = eddigito2.text.toString().toIntOrNull() ?: 0

            txresultado.text = OpMatematica.dividirValores(numero1, numero2)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}