package models

import java.util.UUID

data class Activity(
    var type: String = "",
    var location: String = "",
    var distance: Float = 0.0f,
    var id: String = UUID.randomUUID().toString(),
    val route: MutableList<Location> = ArrayList())