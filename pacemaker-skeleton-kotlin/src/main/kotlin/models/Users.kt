package models

import java.util.UUID

data class User(
    val firstname: String = "",
    val lastname: String = "",
    val email: String = "",
    val password: String = "",
    var id: String = UUID.randomUUID().toString(),
    val activities: MutableMap<String, Activity> = hashMapOf<String, Activity>())