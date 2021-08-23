package com.niky.proyectohrc

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.niky.proyectohrc.adapters.AulasListAdapter
import com.niky.proyectohrc.entities.Aula
import org.w3c.dom.Text

class main : Fragment() {

    //private val TAG = "ReadAndWriteSnippets"

    private lateinit var myRef: DatabaseReference
    private lateinit var database : FirebaseDatabase

    lateinit var v : View
    lateinit var recAulas: RecyclerView

    var aulas : MutableList<Aula> = ArrayList<Aula>()
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var aulasListAdapter: AulasListAdapter
    lateinit var tvPrueba: TextView
    lateinit var pbCarga: ProgressBar
    lateinit var btnInfoCOvid: Button

    companion object {
        fun newInstance() = main()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_main, container, false)

        recAulas = v.findViewById(R.id.rec_aulas)
        tvPrueba = v.findViewById(R.id.txt_prueba)
        pbCarga = v.findViewById(R.id.pb_loading)
        btnInfoCOvid = v.findViewById(R.id.btn_info)

        tvPrueba.visibility = View.VISIBLE
        pbCarga.visibility = View.VISIBLE

        myRef = FirebaseDatabase.getInstance().getReference("ESP32s")
        myRef.addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    aulas.clear()

                    for (esp32Snapshot in snapshot.children)
                    {
                        val valorEspEncontrado = esp32Snapshot.getValue().toString()


                        val co2Encontrado = valorEspEncontrado.substringBefore("-").toString()
                        val aulaEncontrada = valorEspEncontrado.subSequence(co2Encontrado.length+1, valorEspEncontrado.lastIndexOf("-")).toString()
                        val orientacionEncontrada = valorEspEncontrado.subSequence(valorEspEncontrado.lastIndexOf("-")+1, valorEspEncontrado.length).toString()

                        Log.d(TAG, "CO2: " + co2Encontrado)
                        Log.d(TAG, "AULA: " + aulaEncontrada)
                        Log.d(TAG, "ORIENTACION: " + orientacionEncontrada)

                        aulas.add(Aula(co2Encontrado, aulaEncontrada, orientacionEncontrada))
                    }

                    tvPrueba.visibility = View.INVISIBLE
                    pbCarga.visibility = View.INVISIBLE

                    recAulas.setHasFixedSize(true)

                    recAulas.layoutManager = LinearLayoutManager(context)
                    linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    recAulas.layoutManager = linearLayoutManager

                    aulasListAdapter = AulasListAdapter(aulas) { x -> onItemClick(x)}

                    recAulas.adapter = aulasListAdapter
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return v
    }

    override fun onStart() {
        super.onStart()

        btnInfoCOvid.setOnClickListener{

            val builder =  AlertDialog.Builder(context)
            builder.setTitle("¿Como nos cuidamos en ORT?")
            builder.setMessage("\nEn ORT se toman todas las medidas posibles para detener el avance del COVID-19. Por ejemplo, las aulas permanecen con las puertas y las ventanas abiertas para permitir la ciculación del aire, se hace obligatorio el uso del barbijo o tapaboca y no se permite mezclar burbujas. Ante algún caso sospechoso o confirmado, se aisla al grupo afectado entero, con el propósito de no seguir propagando el virus. \n\n¡Acordate que la única manera de superar la pandemia es colaborando todos juntos!")
            builder.setPositiveButton("¡Ok!", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }


    }

    fun onItemClick ( position : Int ) : Boolean {
        return true
    }
}