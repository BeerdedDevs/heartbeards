package io.beerdeddevs.heartbeards.feature.timeline

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import io.beerdeddevs.heartbeards.R

class TimelineAdapter(context: Context, options: FirebaseRecyclerOptions<TimelineItem>) :
        FirebaseRecyclerAdapter<TimelineItem, TimelineAdapter.ViewHolder>(options) {

    private val picasso: Picasso = Picasso.with(context)
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: TimelineItem) {
        holder.nameTextView.text = model.name
        picasso.load(model.imageUrl).into(holder.beardImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(layoutInflater.inflate(R.layout.recycler_item_timeline, parent, false))

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val beardImageView: ImageView = view.findViewById(R.id.beardImageView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
    }

}