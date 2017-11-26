package io.beerdeddevs.heartbeards.feature.timeline

class TimelineItem {
    var name: String? = null
    var imageUrl: String? = null

    constructor() // necessary for firebase

    constructor(name: String, imageUrl: String) {
        this.name = name
        this.imageUrl = imageUrl
    }
}