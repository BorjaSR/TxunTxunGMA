package es.bsalazar.txuntxungma.app.rates

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import es.bsalazar.txuntxungma.R
import es.bsalazar.txuntxungma.app.base.lists.BaseAdapter
import es.bsalazar.txuntxungma.domain.entities.Rate
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class RatesAdapter : BaseAdapter<Rate>() {

    var onEditRate: OnEditRate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false);
        return RateViewHolder(v)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RateViewHolder).let {
            holder.container.setOnLongClickListener {
                onEditRate?.onEditRate(list[holder.adapterPosition])
                return@setOnLongClickListener true
            }

            holder.description.text = list[holder.adapterPosition].description

            val formatoImporte = NumberFormat.getCurrencyInstance(Locale("es", "ES"))
            holder.amount.text = formatoImporte.format(list[holder.adapterPosition].amount)
        }
    }

    override fun areSameItem(item: Rate, item2: Rate) = item.id == item2.id

    fun getItem(position: Int) = list[position]

    inner class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var container: CardView = itemView.findViewById(R.id.container)
        var description: TextView = itemView.findViewById(R.id.rate_description)
        var amount: TextView = itemView.findViewById(R.id.rate_amount)
    }

    interface OnEditRate {
        fun onEditRate(rate: Rate)
    }
}