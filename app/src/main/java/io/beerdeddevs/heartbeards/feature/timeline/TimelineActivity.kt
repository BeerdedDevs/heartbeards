package io.beerdeddevs.heartbeards.feature.timeline

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.beerdeddevs.heartbeards.R

class TimelineActivity : AppCompatActivity() {

    private lateinit var adapter: TimelineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        adapter = TimelineAdapter(this@TimelineActivity)
        findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(this@TimelineActivity)
            adapter = this@TimelineActivity.adapter
        }

        adapter.addAll(listOf(TimelineItem("Someone1", "http://via.placeholder.com/350x650"),
                TimelineItem("Someone2", "http://via.placeholder.com/500x350"),
                TimelineItem("Someone3", "http://via.placeholder.com/450x750")))
    }

}