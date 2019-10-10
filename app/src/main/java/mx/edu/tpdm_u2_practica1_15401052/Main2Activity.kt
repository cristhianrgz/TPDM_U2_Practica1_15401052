package mx.edu.tpdm_u2_practica1_15401052

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main2.*
import java.sql.SQLException
import kotlin.random.Random

class Main2Activity : AppCompatActivity() {

    var descripLista : EditText ?= null
    var fechaLista : EditText ?= null
    var insertarLista : Button ?= null
    var regresar : Button ?= null
    var actualizar : Button ?= null
    var etiquetaMos : TextView ?= null
    var buscar : Button ?= null
    var cont = 0
    var basedatos = BaseDatos(this,"practica1", null, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        descripLista = findViewById(R.id.editDescripLista)
        fechaLista = findViewById(R.id.editFecha)
        insertarLista = findViewById(R.id.btnInsertarLista2)
        regresar = findViewById(R.id.btnRegresar)
        actualizar = findViewById(R.id.btnActualizarLista)
        etiquetaMos = findViewById(R.id.etiquetaMostrar)
        buscar = findViewById(R.id.btnBuscarLista)

        insertarLista?.setOnClickListener {
            insertar()
        }

        buscar?.setOnClickListener {
            pedirID(buscar?.text.toString())
        }

        actualizar?.setOnClickListener {
            if(actualizar?.text.toString().startsWith("Actualizar")){
                pedirID(actualizar?.text.toString())
            }
            else{
                aplicarCambios()
            }
        }

        regresar?.setOnClickListener {
            finish()
        }

    }

    //FUNCION INSERTAR LISTAS
    fun insertar(){
        try {
                var transacion = basedatos.writableDatabase
                var SQL = "INSERT INTO LISTA VALUES(null,'DESCRIPCION','FECHACREACION')"

                if(validarCampos() == false){
                    mensaje("ERROR", "AL PARECER HAY UN CAMPO DE TEXTO VACIO")
                    return
                }

                SQL = SQL.replace("DESCRIPCION",editDescripLista?.text.toString())
                SQL = SQL.replace("FECHACREACION",editFecha?.text.toString())
                transacion.execSQL(SQL)
                mensaje("EXITO", "SE INSERTO CORRECTAMENTE ")
                transacion.close()

                mensaje("EXITO", "SE INSERTO CORRECTAMENTE")
                limpiarCampos()
        }catch (err: SQLiteException){
            mensaje("Error", "NO SE PUDO INSERTAR TALVEZ EL ID YA EXISTE")
        }
    }

    fun pedirID(etiqueta:String){
        var campo = EditText(this)
        campo.inputType = InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this).setTitle("ATENCION").setMessage("ESCRIBA EL ID A  ${etiqueta}: ").setView(campo)
            .setPositiveButton("OK"){dialog,which->
                if(validarCampo(campo) == false){
                    Toast.makeText(this@Main2Activity, "ERROR CAMPO VACÍO", Toast.LENGTH_LONG).show()
                    return@setPositiveButton
                }
                buscar(campo.text.toString(),etiqueta)


            }.setNeutralButton("CANCELAR"){dialog, which ->  }.show()
    }

    fun buscar(id:String, btnEtiqueta: String){
        try {
            var transaccion = basedatos.readableDatabase
            var SQL="SELECT *  FROM LISTA WHERE IDLISTA="+id

            var  respuesta = transaccion.rawQuery(SQL,null)

            if (respuesta.moveToFirst()==true){
                var cadena = "DESCRIPCION: " + respuesta.getString(1)+"\nFECHA: "+respuesta.getString(2)

                //DEPENDIENDO EL BOTON PRESIONADO (BUSCAR, ELIMINAR O ACTUALIZAR) ES LA ACCION QUE REALIZARA
                if (btnEtiqueta.startsWith("Buscar")) {
                    etiquetaMos?.setText(cadena)
                }

                if (btnEtiqueta.startsWith("Eliminar")){
                    var cadena = "SEGURO QUE DESEA ELIMINAR A LA LISTA [ "+respuesta.getString(1)+" ] CON ID [ "+respuesta.getString(0)+" ]"
                    var alerta = AlertDialog.Builder(this)
                    AlertDialog.Builder(this)
                    alerta.setTitle("ATENCION").setMessage(cadena).setNeutralButton("NO"){dialog,which->
                        return@setNeutralButton
                    }.setPositiveButton("si"){dialog,which->
                        //eliminar(id)
                    }.show()
                }

                if(btnEtiqueta.startsWith("Actualizar")){
                    descripLista?.setText(respuesta.getString(1))
                    fechaLista?.setText(respuesta.getString(2))
                    actualizar?.setText("Aplicar cambios")
                    insertarLista?.setEnabled(false)
                }
            }else{
                mensaje("ERROR","NO EXISTE EL ID")
            }
        }catch (err: SQLException){
            mensaje("ERROR","NO SE PUDO ENCONTRAR EL REGISTRO")
        }

    }

    fun actualizar(){
        try {
            var transaction = basedatos.writableDatabase
            var SQL = "UPDATE LISTA SET DESCRIPCION='campodescrip', FECHACREACION='campofecha' WHERE IDLISTA=campoid"
            if (validarCampos()==false){
                mensaje("ERROR","ALGUN CAMPO ESTA VACIO")
                return
            }

            SQL = SQL.replace("DESCRIPCION",descripLista?.text.toString())
            SQL = SQL.replace("FECHACREACION",fechaLista?.text.toString())
            transaction.execSQL(SQL)
            transaction.close()
            desbloquear()
            mensaje("EXITO","Se actualizo correctamente")
        }catch (err:SQLiteException){
            mensaje("ERROR", "No se pudo actualizar el registro")
        }
    }

    fun aplicarCambios(){
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage("¿Estas seguro que deseas aplicar todos los cambios?")
            .setNeutralButton("NO actualizar"){dialog, which -> desbloquear()}
            .setPositiveButton("SI, actualizar"){dialog, which -> actualizar()}.show()
    }

    fun desbloquear(){
        actualizar?.setText("Actualizar")
        insertarLista?.isEnabled = true
        limpiarCampos()
    }

    //VALIDACIONES
    fun  validarCampos():Boolean{
        if(editDescripLista?.text!!.isEmpty() || editFecha?.text!!.isEmpty()){
            return false
        }else{
            return true
        }
    }

    fun validarCampo(campo: EditText): Boolean{
        if(campo.text.toString().isEmpty()){
            return false
        }else{
            return true
        }
    }

    fun limpiarCampos(){
        editDescripLista?.setText("")
        editFecha?.setText("")
    }

    //función para AlertDialogs
    fun mensaje(titulo:String, mensaje:String){
        AlertDialog.Builder(this).setTitle(titulo).setMessage(mensaje).setPositiveButton("Ok"){ dialog, which -> }.show()
    }
}
