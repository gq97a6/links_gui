package net.hostunit.classes

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    var uuid: String = "",
    var username: String = "",
    var score: MutableList<Int> = MutableList(5) { 0 }
)