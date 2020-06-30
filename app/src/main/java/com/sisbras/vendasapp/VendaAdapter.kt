package com.sisbras.vendasapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.venda_item.view.*

class VendaAdapter(
    val Venda: ArrayList<Venda>,
    val itemClick: (Venda) -> Unit
) :
    RecyclerView.Adapter<VendaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.venda_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(Venda[position])
    }

    override fun getItemCount() = Venda.size

    class ViewHolder(view: View, val itemClick: (Venda) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindForecast(Venda: Venda) {
            with(Venda) {
                itemView.txtVenda.text = Venda.nome
                itemView.txtPreco.setAmount(Venda.valor!!.toFloat())
                itemView.txtQtd.text = Venda.qtd.toString()
                //itemView.setOnClickListener { itemClick(this) }
                //itemView.btnDividirConta.setOnClickListener { itemClick(this) }
            }
        }
    }
}
