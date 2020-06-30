package com.sisbras.vendasapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.resumo_layout.*

class MainActivity : AppCompatActivity() {

    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listaVendas()

        btnResumo.setOnClickListener {
            geraResumo()

        }

    }

    private fun geraResumo() {
        Log.i("Teste", "teste")
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("venda")

        val listaResumo: ArrayList<Venda> = arrayListOf()
        var totValor: Double? = 0.0
        var totQtd: Int? = 0
        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listaResumo.clear()
                for (data in dataSnapshot.children) {

                    val vendaData = data.getValue<Venda>(Venda::class.java)
                    //unwrap
                    val venda = vendaData?.let { it } ?: continue
                    venda.let { listaResumo.add(it) }
                }

                listaResumo.forEach {
                    totValor = totValor?.plus(it.valor!!)
                    totQtd = totQtd?.plus(it.qtd!!)
                }
                Log.i("Teste valot",totValor.toString())
                Log.i("Teste total",totQtd.toString())
                val mDialogView = LayoutInflater.from(this@MainActivity).inflate(R.layout.resumo_layout,rootView,false )
                val mBuilder = AlertDialog.Builder(this@MainActivity)
                    .setView(mDialogView)
                    .setTitle("Resumo das Vendas")
                val  mAlertDialog = mBuilder.show()
                mAlertDialog.txtValorTot.setAmount(totValor!!.toFloat())
                mAlertDialog.txtQtdTot.text = totQtd.toString()
                mAlertDialog.btnFechar.setOnClickListener {
                    mAlertDialog.dismiss()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("Erro", databaseError.toString())
            }
        }
        mDatabaseReference?.addListenerForSingleValueEvent(postListener)
    }

    private fun listaVendas() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("venda")

        val toReturn: ArrayList<Venda> = arrayListOf()
        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                toReturn.clear()
                for (data in dataSnapshot.children) {

                    val vendaData = data.getValue<Venda>(Venda::class.java)
                    //unwrap
                    val venda = vendaData?.let { it } ?: continue
                    venda.let { toReturn.add(it) }
                }
                toReturn.sortBy { venda ->
                    venda.id
                }
                setupMesaAdapter(toReturn)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("Teste", databaseError.toString())
            }
        }
        mDatabaseReference?.addValueEventListener(postListener)
    }


    private fun setupMesaAdapter(toReturn: ArrayList<Venda>) {

        rvVendas.clearOnChildAttachStateChangeListeners()
        val linearLayoutManager = LinearLayoutManager(this)
        rvVendas.layoutManager = linearLayoutManager
        rvVendas.adapter = VendaAdapter(toReturn, this::act)
        //scroll to bottom
        rvVendas.scrollToPosition(toReturn.size - 1)

    }

    private fun act (data : Venda) : Unit {
        var txtPin: String? = null
    }
}