package net.hostunit.classes

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var username: String = "",
    var password: String = ""
)