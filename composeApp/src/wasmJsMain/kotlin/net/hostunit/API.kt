package net.hostunit

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.hostunit.classes.Address
import net.hostunit.classes.User

object API {
    private const val URL = "http://localhost"

    private val client = HttpClient(Js) {
        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }
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

    suspend fun getAddress(code: String, onFail: (HttpStatusCode?) -> Unit = {}): Address {
        try {
            val response = client.get("$URL/address/$code")
            if (response.status == HttpStatusCode.OK) return response.body()

            onFail(response.status)
            return Address()
        } catch (e: Exception) {
            onFail(null)
            return Address()
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun postAddress(address: Address, onFail: (HttpStatusCode?) -> Unit = {}): String {
        try {
            val jsonBody = Json.encodeToString(address)

            val response = client.post("$URL/address") {
                contentType(ContentType.Application.Json)
                body = jsonBody
            }

            if (response.status == HttpStatusCode.OK) return response.body()

            onFail(response.status)
            return ""
        } catch (e: Exception) {
            onFail(null)
            return ""
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun putAddress(address: Address, onFail: (HttpStatusCode?) -> Unit = {}): Boolean {
        try {
            val jsonBody = Json.encodeToString(address)

            val response = client.put("$URL/address/${address.code}") {
                contentType(ContentType.Application.Json)
                body = jsonBody
            }

            if (response.status == HttpStatusCode.OK) return response.body()

            onFail(response.status)
            return false
        } catch (e: Exception) {
            onFail(null)
            return false
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun login(user: User, onFail: (HttpStatusCode?) -> Unit = {}): Boolean {
        try {
            val jsonBody = Json.encodeToString(user)

            val response = client.post("$URL/login") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Cookie, "withCredentials=true")
                body = jsonBody
            }

            if (response.status == HttpStatusCode.OK) return true

            onFail(response.status)
            return false
        } catch (e: Exception) {
            println(e.message)
            onFail(null)
            return false
        }
    }

    suspend fun loginToken(onFail: (HttpStatusCode?) -> Unit = {}): Boolean {
        try {
            val response = client.post("$URL/login")
            if (response.status == HttpStatusCode.OK) return true

            onFail(response.status)
            return false
        } catch (e: Exception) {
            onFail(null)
            return false
        }
    }

    //suspend fun getList(): List<Address>? {
    //    return try {
    //        val response = client.get("$URL/list")
    //        return if (response.status == HttpStatusCode.OK) response.body() else null
    //    } catch (e: Exception) {
    //        null
    //    }
    //}
}