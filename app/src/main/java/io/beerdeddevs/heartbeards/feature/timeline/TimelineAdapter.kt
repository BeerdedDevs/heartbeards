package io.beerdeddevs.heartbeards.feature.timeline

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.beerdeddevs.heartbeards.R

class TimelineAdapter(context: Context) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    private val picasso: Picasso = Picasso.with(context)
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val collection = ArrayList<TimelineItem>()

    fun addAll(list: List<TimelineItem>) {
        collection.addAll(list)
        notifyItemRangeInserted(collection.size - list.size, list.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.text = collection[position].name
        picasso.load(collection[position].imageUrl).into(holder.beardImageView)
    }

    override fun getItemCount(): Int = collection.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(layoutInflater.inflate(R.layout.recycler_item_timeline, parent, false))

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val beardImageView: ImageView = view.findViewById(R.id.beardImageView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
    }

}