package es.bsalazar.txuntxungma.app.rates

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import es.bsalazar.txuntxungma.R
import es.bsalazar.txuntxungma.domain.entities.Rate
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class RatesAdapter : RecyclerView.Adapter<RatesAdapter.RateViewHolder>() {

    private var rates: ArrayList<Rate> = ArrayList()
    lateinit var onEditRate: OnEditRate

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false);
        return RateViewHolder(v)
    }

    override fun getItemCount(): Int = rates.size

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {

        holder.container.setOnLongClickListener {
            if (onEditRate != null) {
                onEditRate.onEditRate(rates[holder.adapterPosition])
                return@setOnLongClickListener true
            }
            false
        }

        holder.description.text = rates[holder.adapterPosition].description

        val formatoImporte = NumberFormat.getCurrencyInstance(Locale("es", "ES"))
        holder.amount.text = formatoImporte.format(rates[holder.adapterPosition].amount)
    }

    fun setRates(rates: List<Rate>) {
        this.rates = rates as ArrayList<Rate>
        notifyDataSetChanged()
    }


    internal fun addRate(index: Int, rate: Rate) {
        if (!containRate(rate)) {
            rates.add(index, rate)
            notifyItemInserted(index)
        }
    }

    internal fun modifyRate(index: Int, rate: Rate) {
        if (index < rates.size) {
            rates.set(index, rate)
            notifyItemChanged(index)
        }
    }

    internal fun removeRate(index: Int, rate: Rate) {
        if (index < rates.size && containRate(rate)) {
            rates.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    private fun containRate(rate: Rate): Boolean {
        for (component1 in rates)
            if (rate.id == component1.getId())
                return true
        return false
    }

    fun getItem(position: Int): Rate = rates[position]

    inner class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var container: CardView = itemView.findViewById(R.id.container)
        var description: TextView = itemView.findViewById(R.id.rate_description)
        var amount: TextView = itemView.findViewById(R.id.rate_amount)
    }

    interface OnEditRate {
        fun onEditRate(rate: Rate)
    }
}