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
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import io.beerdeddevs.heartbeards.R

class TimelineAdapter(context: Context, options: FirebaseRecyclerOptions<TimelineItem>) :
        FirebaseRecyclerAdapter<TimelineItem, TimelineAdapter.ViewHolder>(options) {

    private val picasso = Picasso.Builder(context).downloader(OkHttp3Downloader(context)).build()
    private val layoutInflater = LayoutInflater.from(context)

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: TimelineItem) {
        holder.nameTextView.text = model.name
        picasso.load(model.imageUrl)
            .rotate(-90.0f) // There's an issue with the Exif orientation and this is a super hacky bug in order to show the image properly.
            .into(holder.beardImageView)
        holder.favIcon.setOnClickListener {
            holder.favIcon.setImageResource(R.drawable.ic_fav_full)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(layoutInflater.inflate(R.layout.recycler_item_timeline, parent, false))

    override fun getItemId(position: Int) = getItem(position).id.hashCode().toLong()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val beardImageView: ImageView = view.findViewById(R.id.beardImageView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val favIcon: ImageView = view.findViewById(R.id.likeButton)
    }
}
