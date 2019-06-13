package es.bsalazar.txuntxungma.app.events

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import es.bsalazar.txuntxungma.R
import es.bsalazar.txuntxungma.domain.entities.Event
import es.bsalazar.txuntxungma.firstUpperCase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.animation.ValueAnimator
import android.animation.ArgbEvaluator
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.animation.Animation
import android.widget.Toast


class EventsAdapter : RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    lateinit var context: Context

    val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dayFormat: SimpleDateFormat = SimpleDateFormat("dd", Locale.getDefault())
    val dayOfWeekFormat: SimpleDateFormat = SimpleDateFormat("EEEE", Locale("es", "ES"))
    val monthFormat: SimpleDateFormat = SimpleDateFormat("MMMM", Locale("es", "ES"))

    var onEditEvent: OnEditEvent? = null
    var notificationEventId = ""

    private var events: ArrayList<Event> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false);
        return EventsViewHolder(v)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.context = recyclerView.context
    }

    override fun getItemCount(): Int = events.size

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {

        val event = events[holder.adapterPosition]

        if (event.id!! == notificationEventId) {
            animateBackground(holder.container)
            notificationEventId = ""
        }

        holder.container.setOnClickListener {
            onEditEvent?.onCompactChangeEvent(holder.adapterPosition, event.id!!)
        }

        holder.container.setOnLongClickListener {
            if (onEditEvent != null) {
                onEditEvent?.onEditEvent(event)
                return@setOnLongClickListener true
            }
            false
        }

        val date = Date(event.date)
        holder.day.text = dayFormat.format(date)
        holder.month.text = monthFormat.format(date).firstUpperCase()
        holder.hour.text = hourFormat.format(date)
        holder.dayWeek.text = dayOfWeekFormat.format(date).firstUpperCase()

        holder.name.text = event.name
        holder.description.text = event.description

        if (event.compacted) {
            holder.description.maxLines = 3
            holder.description.ellipsize = TextUtils.TruncateAt.END;
        } else {
            holder.description.maxLines = Integer.MAX_VALUE
            holder.description.ellipsize = null;
        }

        if (event.alarmActivated)
            holder.activateAlarm.setImageDrawable(context.getDrawable(R.drawable.bell))
        else
            holder.activateAlarm.setImageDrawable(context.getDrawable(R.drawable.bell_off_outline))

        holder.activateAlarm.setOnClickListener {
            if (event.alarmActivated)
                onEditEvent?.onDefuseAlarm(event)
            else
                onEditEvent?.onActivateAlarm(event)
        }
    }

    fun animateBackground(view: CardView) {
        val colorFrom = ContextCompat.getColor(context, R.color.white)
        val colorTo = ContextCompat.getColor(context, R.color.colorPrimaryAlmostWhite)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 200
        colorAnimation.repeatCount = 3
        colorAnimation.repeatMode = ValueAnimator.REVERSE
        colorAnimation.addUpdateListener { animator -> view.setCardBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.start()
    }

    fun setEvents(events: List<Event>) {
        this.events = events as ArrayList<Event>
        notifyDataSetChanged()
    }


    internal fun addEvent(index: Int, event: Event) {
        if (!containEvent(event)) {
            events.add(index, event)
            notifyItemInserted(index)
        }
    }

    var timerID = -1
    internal fun modifyEvent(index: Int, event: Event) {
        if (index < events.size) {
            events.set(index, event)
            notifyItemChanged(index)

//            if (timerID != index) {
//                MyTimer(index, event, this::checkPositionOfEvent).start()
//                timerID = index
//            }
        }
    }

    private fun checkPositionOfEvent(index: Int, event: Event) {
        var newPosition = 0;
        for (i in 0..events.size) {
            if (event.date < events[i].date) {
                newPosition = i
                break
            }
        }

        if (newPosition != index) {
            if (newPosition > index) newPosition--
            notifyItemMoved(index, newPosition)
        }
        timerID = -1
    }

    internal fun removeEvent(index: Int, event: Event) {
        if (index < events.size && containEvent(event)) {
            events.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    private fun containEvent(event: Event): Boolean {
        for (eventAux in events)
            if (event.id == eventAux.id)
                return true
        return false
    }

    fun getItem(position: Int): Event = events[position]

    inner class EventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var container: CardView = itemView.findViewById(R.id.container_event)
        var day: TextView = itemView.findViewById(R.id.event_day)
        var month: TextView = itemView.findViewById(R.id.event_month)
        var hour: TextView = itemView.findViewById(R.id.event_hour)
        var dayWeek: TextView = itemView.findViewById(R.id.event_day_of_week)
        var name: TextView = itemView.findViewById(R.id.event_name)
        var description: TextView = itemView.findViewById(R.id.event_description)
        var activateAlarm: ImageView = itemView.findViewById(R.id.activate_alarm)
    }

    interface OnEditEvent {

        fun onCompactChangeEvent(position: Int, eventId: String)

        fun onEditEvent(event: Event)

        fun onActivateAlarm(event: Event)

        fun onDefuseAlarm(event: Event)
    }

    class MyTimer(val index: Int, val event: Event, val function: (Int, Event) -> (Unit)) : Thread() {

        override fun run() {
            Thread.sleep(500)
            function(index, event)
        }
    }
}