package es.bsalazar.txuntxungma.app.pruebas

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import es.bsalazar.txuntxungma.R
import es.bsalazar.txuntxungma.app.base.lists.BaseAdapter
import es.bsalazar.txuntxungma.domain.entities.Event

class PruebaAdapter : BaseAdapter<Event>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PruebaViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false);
        return PruebaViewHolder(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Bind view
    }

    override fun areSameItem(item: Event, item2: Event) = item.id == item2.id

    inner class PruebaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var container: CardView = itemView.findViewById(R.id.container_event)
        var day: TextView = itemView.findViewById(R.id.event_day)
        var month: TextView = itemView.findViewById(R.id.event_month)
        var hour: TextView = itemView.findViewById(R.id.event_hour)
        var dayWeek: TextView = itemView.findViewById(R.id.event_day_of_week)
        var name: TextView = itemView.findViewById(R.id.event_name)
        var description: TextView = itemView.findViewById(R.id.event_description)
        var activateAlarm: ImageView = itemView.findViewById(R.id.activate_alarm)
    }
}