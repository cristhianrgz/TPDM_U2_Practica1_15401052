package mx.edu.tpdm_u2_practica1_15401052

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.sql.SQLException

class MainActivity : AppCompatActivity() {

    var btnInsertLista : Button ?= null
    var mostrarListas : ListView ?= null
    var descripTarea : EditText ?= null
    var realizadoTarea : EditText ?= null
    var btnInsertTareas : Button ?= null


    var basedatos = BaseDatos(this,"practica1", null, 1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnInsertLista = findViewById(R.id.btnInsertLista)
        mostrarListas = findViewById(R.id.listasCreadas)
        descripTarea = findViewById(R.id.editDescripTarea)
        realizadoTarea = findViewById(R.id.editRealizado)
        btnInsertTareas = findViewById(R.id.btnInsertarTarea)

        btnInsertLista?.setOnClickListener {
            val ventanaInsert = Intent(this, Main2Activity::class.java)
            startActivity(ventanaInsert)
        }

        btnInsertTareas?.setOnClickListener{

        }

    }


}
