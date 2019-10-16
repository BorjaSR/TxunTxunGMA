package es.bsalazar.txuntxungma.app.base.lists

import android.support.v7.widget.RecyclerView

open abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var list: ArrayList<T> = ArrayList()

    fun setItems(events: List<T>) {
        this.list = events as ArrayList<T>
        notifyDataSetChanged()
    }

    fun addItem(index: Int, item: T) {
        if (!containItem(item)) {
            list.add(index, item)
            notifyItemInserted(index)
        }
    }

    fun modifyItem(index: Int, item: T) {
        if (index < list.size) {
            list.set(index, item)
            notifyItemChanged(index)
        }
    }

    fun removeItem(index: Int, item: T) {
        if (index < list.size && containItem(item)) {
            list.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    private fun containItem(item: T): Boolean {
        for (item1 in list)
            if (areSameItem(item, item1))
                return true
        return false
    }

    abstract fun areSameItem(item: T, item2: T) : Boolean
}