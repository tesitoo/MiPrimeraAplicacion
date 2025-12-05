package com.example.miprimeraaplicacion.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MantenimientoDBHelper(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                tipo TEXT,
                fecha TEXT
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // ------------------ CRUD LOCAL ------------------

    fun insertar(nombre: String, tipo: String, fecha: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("tipo", tipo)
            put("fecha", fecha)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun obtenerTodos(): List<String> {
        val lista = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                lista.add("$id: $nombre - $tipo - $fecha")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizar(id: Int, nombre: String, tipo: String, fecha: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("tipo", tipo)
            put("fecha", fecha)
        }
        val rows = db.update(TABLE_NAME, values, "id=?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    fun eliminar(id: Int): Int {
        val db = writableDatabase
        val rows = db.delete(TABLE_NAME, "id=?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    companion object {
        private const val DATABASE_NAME = "mantenimientos.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "mantenimientos"
    }
}
