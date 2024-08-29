package net.hostunit.classes

import kotlinx.serialization.Serializable

@Serializable
data class Match(
    var index: Int = 0,
    var teamRed: MutableList<Player> = MutableList(5) { Player() },
    var teamBlue: MutableList<Player> = MutableList(5) { Player() },
    var scoreSum: Double = .0,
    var zeroCount: Double = .0,
    var laneDiff: Double = .0,
    var teamDiff: Double = .0
)