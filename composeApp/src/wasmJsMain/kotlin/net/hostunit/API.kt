package net.hostunit

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.hostunit.classes.Match
import net.hostunit.classes.Player
import net.hostunit.classes.User

object API {
    private const val URL = "https://team.hostunit.net"

    //private const val URL = "http://localhost"
    private val client = HttpClient(Js) {
        install(ContentNegotiation) {
            json(Json {
                encodeDefaults = true
                isLenient = true
                allowSpecialFloatingPointValues = true
                allowStructuredMapKeys = true
                prettyPrint = false
                useArrayPolymorphism = false
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getCurrentMatch(): Match {
        return try {
            val response = client.get("$URL/api/match/current")
            return if (response.status == HttpStatusCode.OK) response.body<Match>() else Match()
        } catch (e: Exception) {
            println(e.message)
            window.alert("ERROR")
            Match()
        }
    }

    suspend fun getRandomMatch(): Match {
        return try {
            val response = client.get("$URL/api/match/random")
            return if (response.status == HttpStatusCode.OK) response.body<Match>() else Match()
        } catch (e: Exception) {
            println(e.message)
            window.alert("ERROR")
            Match()
        }
    }

    suspend fun postMatch(index: Int) {
        try {
            val response = client.post("$URL/api/match/current") {
                setBody("$index")
            }
            window.alert(if (response.status == HttpStatusCode.OK) "OK" else "ERROR")
        } catch (e: Exception) {
            println(e.message)
            window.alert("ERROR")
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun postPlayers(players: List<Player>) {
        try {
            // Manually serialize the user object to JSON string
            val jsonBody = Json.encodeToString(players)

            val response = client.post("$URL/api/player") {
                contentType(ContentType.Application.Json)
                body = jsonBody
            }
            window.alert(if (response.status == HttpStatusCode.OK) "OK" else "ERROR")
        } catch (e: Exception) {
            println(e.message)
            window.alert("ERROR")
        }
    }

    suspend fun getPlayers(): List<Player> {
        return try {
            val response = client.get("$URL/api/player")
            return if (response.status == HttpStatusCode.OK) response.body<List<Player>>() else listOf()
        } catch (e: Exception) {
            println(e.message)
            window.alert("ERROR")
            listOf()
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun getPlayerSummary(uuids: List<String>): String {
        return try {
            // Manually serialize the user object to JSON string
            val jsonBody = Json.encodeToString(uuids)

            val response = client.post("$URL/api/player/text") {
                contentType(ContentType.Application.Json)
                body = jsonBody
            }

            if (response.status == HttpStatusCode.OK) response.body<String>() else ""
        } catch (e: Exception) {
            println(e.message)
            window.alert("ERROR")
            ""
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun postPool(uuids: List<String>) {
        try {
            // Manually serialize the user object to JSON string
            val jsonBody = Json.encodeToString(uuids)

            val response = client.post("$URL/api/pool") {
                contentType(ContentType.Application.Json)
                body = jsonBody
            }
            window.alert(if (response.status == HttpStatusCode.OK) "OK" else "ERROR")
        } catch (e: Exception) {
            println(e.message)
            window.alert("ERROR")
        }
    }

    suspend fun getPool(): List<Match> {
        return try {
            val response = client.get("$URL/api/pool")
            return if (response.status == HttpStatusCode.OK) response.body<List<Match>>() else listOf()
        } catch (e: Exception) {
            println(e.message)
            window.alert("ERROR")
            listOf()
        }
    }

    suspend fun getPoolSummary(): String {
        return try {
            val response = client.get("$URL/api/pool")
            return if (response.status == HttpStatusCode.OK) response.body<String>() else ""
        } catch (e: Exception) {
            println(e.message)
            window.alert("ERROR")
            ""
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun login(user: User) {
        try {
            // Manually serialize the user object to JSON string
            val jsonBody = Json.encodeToString(user)

            val response = client.post("$URL/api/auth/login") {
                contentType(ContentType.Application.Json)
                body = jsonBody
            }
            window.alert(if (response.status == HttpStatusCode.OK) "OK" else "ERROR")
        } catch (e: Exception) {
            println(e.message)
            window.alert("ERROR")
        }
    }
}