package net.hostunit.classes

import kotlinx.serialization.Serializable

@Serializable
class Link(
    var payload: String = "",
    var action: Action = Action.TAB,
) {
    @Serializable
    enum class Action { COPY, LINK, TAB }
}