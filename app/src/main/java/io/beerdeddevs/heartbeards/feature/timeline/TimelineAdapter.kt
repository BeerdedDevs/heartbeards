package io.beerdeddevs.heartbeards.feature.timeline

import android.app.Activity
import android.content.Context
import android.graphics.Point
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

class TimelineAdapter(context: Context, options: FirebaseRecyclerOptions<TimelineItem>)
    : FirebaseRecyclerAdapter<TimelineItem, TimelineAdapter.ViewHolder>(options) {

    private val width: Int
    private val height: Int
    private val layoutInflater = LayoutInflater.from(context)
    private val picasso = Picasso.Builder(context)
            .downloader(OkHttp3Downloader(context))
            .build()

    init {
        val display = (context as Activity).windowManager.defaultDisplay
        val size = Point().apply { display.getSize(this) }
        width = size.x - 2 * context.resources.getDimensionPixelSize(R.dimen.content_margin_half)
        height = context.resources.getDimensionPixelSize(R.dimen.timeline_height)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: TimelineItem) {
        holder.nameTextView.text = model.name
        picasso.load(model.imageUrl)
                .resize(width, height)
                .centerCrop()
                .rotate(-90f) // Ouch!
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
