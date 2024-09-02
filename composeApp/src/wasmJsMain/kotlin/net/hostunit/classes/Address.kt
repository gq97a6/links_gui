package net.hostunit.classes

import kotlinx.serialization.Serializable

@Serializable
class Address(
    var id: String = "",
    var direct: Boolean = false,
    var code: String = "",
    var links: MutableList<Link> = mutableListOf()
)