package es.bsalazar.txuntxungma.app.releases

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import es.bsalazar.txuntxungma.domain.entities.Release
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import es.bsalazar.txuntxungma.R

class ReleasesAdapter : RecyclerView.Adapter<ReleasesAdapter.ReleasesViewHolder>() {

    val dateFormat: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))

    private lateinit var context: Context
    private var releases: ArrayList<Release> = ArrayList()
    private var releasesSigned: ArrayList<String> = ArrayList()
    var onEditRelease: OnEditRelease? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReleasesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_releases, parent, false);
        return ReleasesViewHolder(v)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun getItemCount(): Int = releases.size

    override fun onBindViewHolder(holder: ReleasesViewHolder, position: Int) {

        val release = releases[holder.adapterPosition]

        holder.container.setOnLongClickListener {
            if (onEditRelease != null) {
                onEditRelease?.onEditRelease(release)
                return@setOnLongClickListener true
            }
            false
        }
        holder.container.setOnClickListener {
            onEditRelease?.onViewReleaseDetail(release, holder.container)
        }

        val date = Date(release.date)
        holder.date.text = dateFormat.format(date)

        holder.name.text = release.title
        holder.description.text = release.description

        if (release.hasList) {
            holder.sign_up_button.visibility = View.VISIBLE
            if (release.signedByUser) {
                holder.sign_up_button.text = context.resources.getString(R.string.already_signed)
            } else {
                holder.sign_up_button.text = context.resources.getString(R.string.sign_up)
                holder.sign_up_button.setOnClickListener { onEditRelease?.onSignUp(release) }
            }
        } else
            holder.sign_up_button.visibility = View.GONE
    }

    fun setReleases(events: List<Release>) {
        this.releases = events as ArrayList<Release>
        notifyDataSetChanged()
    }


    internal fun addRelease(index: Int, release: Release) {
        if (!containRelease(release)) {
            releases.add(index, release)
            notifyItemInserted(index)
        }
    }

    internal fun modifyRelease(index: Int, release: Release) {
        if (index < releases.size) {
            releases.set(index, release)
            notifyItemChanged(index)
        }
    }

    internal fun removeRelease(index: Int, release: Release) {
        if (index < releases.size && containRelease(release)) {
            releases.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    private fun containRelease(release: Release): Boolean {
        for (eventAux in releases)
            if (release.id == eventAux.id)
                return true
        return false
    }

    fun getItem(position: Int): Release = releases[position]

    inner class ReleasesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var container: CardView = itemView.findViewById(R.id.container_release)
        var name: TextView = itemView.findViewById(R.id.release_name)
        var date: TextView = itemView.findViewById(R.id.release_date)
        var description: TextView = itemView.findViewById(R.id.release_description)
        var sign_up_button: TextView = itemView.findViewById(R.id.sign_up_button)
    }

    interface OnEditRelease {
        fun onViewReleaseDetail(release: Release, view: View)
        fun onEditRelease(release: Release)
        fun onSignUp(release: Release)
    }
}