package io.beerdeddevs.heartbeards.feature.timeline

class TimelineItem {
    var id: String = ""
    var timestamp: Long = 0L
    var name: String = ""
    var imageUrl: String = ""

    constructor() // necessary for firebase

    constructor(id: String, timestamp: Long, name: String, imageUrl: String) {
        this.id = id
        this.timestamp = timestamp
        this.name = name
        this.imageUrl = imageUrl
    }
}