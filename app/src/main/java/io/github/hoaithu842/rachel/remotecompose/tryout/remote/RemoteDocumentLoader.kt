package io.github.hoaithu842.rachel.remotecompose.tryout.remote

import java.net.HttpURLConnection
import java.net.URL

/**
 * Loads a Remote Compose binary document (.rc) from a URL.
 * Use for server-driven UI: point at your deployed document or host (e.g. http://10.0.2.2:8081/remote/... on emulator).
 */
object RemoteDocumentLoader {

    fun loadFromUrl(urlString: String): Result<ByteArray> = runCatching {
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "GET"
            conn.connectTimeout = 15_000
            conn.readTimeout = 15_000
            conn.connect()
            if (conn.responseCode !in 200..299) {
                throw Exception("HTTP ${conn.responseCode}: ${conn.responseMessage}")
            }
            conn.inputStream.readBytes()
        } finally {
            conn.disconnect()
        }
    }

    /** Fetches document from server (GET {baseUrl}/remote/document). Returns .rc byte array (button with picked color). */
    fun fetchDocument(baseUrl: String): Result<ByteArray> = runCatching {
        val url = URL("${baseUrl.trimEnd('/')}/remote/document")
        val conn = url.openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "GET"
            conn.connectTimeout = 15_000
            conn.readTimeout = 15_000
            conn.connect()
            if (conn.responseCode !in 200..299) {
                throw Exception("HTTP ${conn.responseCode}: ${conn.responseMessage}")
            }
            conn.inputStream.readBytes()
        } finally {
            conn.disconnect()
        }
    }

    /** Fetches hello text from server (GET {baseUrl}/remote/hello). Returns e.g. "Hello World". */
    fun fetchHello(baseUrl: String): Result<String> = runCatching {
        val url = URL("${baseUrl.trimEnd('/')}/remote/hello")
        val conn = url.openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "GET"
            conn.connectTimeout = 5_000
            conn.readTimeout = 5_000
            conn.connect()
            if (conn.responseCode !in 200..299) throw Exception("HTTP ${conn.responseCode}")
            conn.inputStream.bufferedReader().readText().trim()
        } finally {
            conn.disconnect()
        }
    }
}
