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
import net.hostunit.classes.Address
import net.hostunit.classes.User

object API {
    private val URL = window.location.let { it.protocol + "//" + it.hostname }

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

    suspend fun getAddress(code: String, onSuccess: suspend (Address) -> Unit = {}): HttpStatusCode? {
        try {
            val response = client.get("$URL/address/$code")
            if (response.status != HttpStatusCode.OK) return response.status
            onSuccess(response.body())
        } catch (e: Exception) {
            return null
        }

        return HttpStatusCode.OK
    }

    @OptIn(InternalAPI::class)
    suspend fun postAddress(address: Address, onSuccess: suspend (Address) -> Unit = {}): HttpStatusCode? {
        checkAuth(address.code)

        try {
            val response = client.post("$URL/address") {
                body = Json.encodeToString(address)
                contentType(ContentType.Application.Json)
            }

            if (response.status != HttpStatusCode.OK) return response.status
            if (response.status == HttpStatusCode.Unauthorized) {
                window.location.href = "/login${address.code}"
                return response.status
            }
            onSuccess(response.body())
        } catch (e: Exception) {
            return null
        }

        return HttpStatusCode.OK
    }

    @OptIn(InternalAPI::class)
    suspend fun putAddress(address: Address, onSuccess: suspend () -> Unit = {}): HttpStatusCode? {
        checkAuth(address.code)

        try {
            val response = client.put("$URL/address/${address.code}") {
                body = Json.encodeToString(address)
                contentType(ContentType.Application.Json)
            }

            if (response.status != HttpStatusCode.OK) return response.status
            if (response.status == HttpStatusCode.Unauthorized) {
                window.location.href = "/login${address.code}"
                return response.status
            }
            onSuccess()
        } catch (e: Exception) {
            return null
        }

        return HttpStatusCode.OK
    }

    suspend fun deleteAddress(code: String, onSuccess: suspend () -> Unit = {}): HttpStatusCode? {
        checkAuth(code)

        try {
            val response = client.delete("$URL/address/$code")
            if (response.status != HttpStatusCode.OK) return response.status
            if (response.status == HttpStatusCode.Unauthorized) {
                window.location.href = "/login$code"
                return response.status
            }
            onSuccess()
        } catch (e: Exception) {
            return null
        }

        return HttpStatusCode.OK
    }

    @OptIn(InternalAPI::class)
    suspend fun login(user: User, onSuccess: suspend () -> Unit = {}): HttpStatusCode? {
        try {
            val response = client.post("$URL/login") {
                body = Json.encodeToString(user)
                contentType(ContentType.Application.Json)
            }

            if (response.status != HttpStatusCode.OK) return response.status
            onSuccess()
        } catch (e: Exception) {
            return null
        }

        return HttpStatusCode.OK
    }

    suspend fun loginToken(onSuccess: suspend () -> Unit = {}): HttpStatusCode? {
        try {
            val response = client.post("$URL/login")
            if (response.status != HttpStatusCode.OK) return response.status
            onSuccess()
        } catch (e: Exception) {
            return null
        }

        return HttpStatusCode.OK
    }

    suspend fun checkAuth(code: String) {
        if (cookieExists("token")) return
        if (!cookieExists("refreshToken")) {
            window.location.href = "/login/$code"
        }

        loginToken() onFail { window.location.href = "/login/$code" }
    }

    suspend infix fun HttpStatusCode?.onFail(action: suspend (HttpStatusCode?) -> Unit) {
        if (this != HttpStatusCode.OK) action(this)
    }
}