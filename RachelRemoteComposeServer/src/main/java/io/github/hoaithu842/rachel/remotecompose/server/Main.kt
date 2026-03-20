package io.github.hoaithu842.rachel.remotecompose.server

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import java.io.File
import java.net.InetSocketAddress
import java.nio.file.Files
import java.util.concurrent.Executors

private const val PORT = 8081
private const val REMOTE_DOCUMENTS_DIR = "documents"

@Volatile
private var remoteColor: String = "#6200EE"

fun main() {
    // Bind to 0.0.0.0 so the emulator (10.0.2.2) and other devices can reach this server.
    val server = HttpServer.create(InetSocketAddress("0.0.0.0", PORT), 0)
    server.executor = Executors.newCachedThreadPool()

    server.createContext("/remote/hello") { exchange ->
        if (exchange.requestMethod != "GET") {
            exchange.sendResponseHeaders(405, -1)
            exchange.close()
            return@createContext
        }
        val body = "Hello World"
        exchange.responseHeaders.add("Content-Type", "text/plain; charset=UTF-8")
        val bytes = body.toByteArray(Charsets.UTF_8)
        exchange.sendResponseHeaders(200, bytes.size.toLong())
        exchange.responseBody.use { it.write(bytes) }
        exchange.close()
    }

    server.createContext("/remote/color") { exchange ->
        when (exchange.requestMethod) {
            "GET" -> {
                val json = """{"color":"$remoteColor"}"""
                exchange.responseHeaders.add("Content-Type", "application/json; charset=UTF-8")
                val bytes = json.toByteArray(Charsets.UTF_8)
                exchange.sendResponseHeaders(200, bytes.size.toLong())
                exchange.responseBody.use { it.write(bytes) }
            }
            "POST" -> {
                val body = exchange.requestBody.bufferedReader().readText()
                val color = body.trim().takeIf { it.matches(Regex("^#[0-9A-Fa-f]{6}$")) }
                    ?: body.split("&").mapNotNull { it.split("=", limit = 2).let { p -> if (p.size == 2) p[0].trim() to java.net.URLDecoder.decode(p[1], "UTF-8") else null } }.toMap()["color"]?.takeIf { it.matches(Regex("^#[0-9A-Fa-f]{6}$")) }
                if (color != null) remoteColor = color
                exchange.sendResponseHeaders(200, -1)
                exchange.responseBody.use { }
            }
            else -> exchange.sendResponseHeaders(405, -1)
        }
        exchange.close()
    }

    server.createContext("/remote/document") { exchange ->
        if (exchange.requestMethod != "GET") {
            exchange.sendResponseHeaders(405, -1)
            exchange.close()
            return@createContext
        }
        // Button document with current picked color (web sets remoteColor via POST /remote/color).
        val bytes = try {
            ButtonDocumentGenerator.buildButtonDocument(remoteColor, "Click Me")
        } catch (e: Exception) {
            e.printStackTrace()
            ByteArray(0)
        }
        if (bytes.isEmpty()) {
            exchange.responseHeaders.add("Content-Type", "text/plain; charset=UTF-8")
            val msg = "Document generation failed. Check server logs."
            val b = msg.toByteArray(Charsets.UTF_8)
            exchange.sendResponseHeaders(500, b.size.toLong())
            exchange.responseBody.use { it.write(b) }
        } else {
            exchange.responseHeaders.add("Content-Type", "application/octet-stream")
            exchange.sendResponseHeaders(200, bytes.size.toLong())
            exchange.responseBody.use { it.write(bytes) }
        }
        exchange.close()
    }

    server.createContext("/remote/documents") { exchange ->
        if (exchange.requestMethod != "GET") {
            exchange.sendResponseHeaders(405, -1)
            exchange.close()
            return@createContext
        }
        val path = exchange.requestURI.path ?: ""
        val name = path.removePrefix("/remote/documents/").trim()
        if (name.isEmpty()) {
            listRemoteDocuments(exchange)
            return@createContext
        }
        val file = File(REMOTE_DOCUMENTS_DIR, name).canonicalFile
        val docsDir = File(REMOTE_DOCUMENTS_DIR).canonicalPath
        if (!file.canonicalPath.startsWith(docsDir) || !file.isFile) {
            exchange.sendResponseHeaders(404, -1)
            exchange.close()
            return@createContext
        }
        serveRemoteDocument(exchange, file)
    }

    server.createContext("/remote") { exchange ->
        if (exchange.requestURI.path != "/remote" && exchange.requestURI.path != "/remote/") {
            exchange.sendResponseHeaders(404, -1)
            exchange.close()
            return@createContext
        }
        val body = remoteInfoPage()
        exchange.responseHeaders.add("Content-Type", "text/html; charset=UTF-8")
        val bytes = body.toByteArray(Charsets.UTF_8)
        exchange.sendResponseHeaders(200, bytes.size.toLong())
        exchange.responseBody.use { it.write(bytes) }
        exchange.close()
    }

    // Root: avoid 403 when client requests "/" (JDK HttpServer returns 403 when no context matches)
    server.createContext("/") { exchange ->
        if (exchange.requestURI.path == "/" || exchange.requestURI.path.isEmpty()) {
            exchange.responseHeaders.add("Location", "/remote")
            exchange.sendResponseHeaders(302, -1)
        } else {
            exchange.sendResponseHeaders(404, -1)
        }
        exchange.responseBody.use { }
        exchange.close()
    }

    server.start()
    println("Remote server: http://localhost:$PORT/remote")
    println("  GET /remote/hello   GET /remote/document (button with picked color)   POST /remote/color (web)   GET /remote/documents")
}

private fun defaultRcFile(): File? {
    val dir = File(REMOTE_DOCUMENTS_DIR)
    if (!dir.exists()) dir.mkdirs()
    return dir.listFiles()?.filter { it.isFile && it.name.endsWith(".rc") }?.minByOrNull { it.name }
}

private fun serveRemoteDocument(exchange: HttpExchange, file: File?) {
    if (file == null || !file.isFile) {
        exchange.responseHeaders.add("Content-Type", "text/plain; charset=UTF-8")
        val msg = "No Remote Compose document. Add a .rc file in $REMOTE_DOCUMENTS_DIR/"
        val bytes = msg.toByteArray(Charsets.UTF_8)
        exchange.sendResponseHeaders(404, bytes.size.toLong())
        exchange.responseBody.use { it.write(bytes) }
    } else {
        val bytes = Files.readAllBytes(file.toPath())
        exchange.responseHeaders.add("Content-Type", "application/octet-stream")
        exchange.sendResponseHeaders(200, bytes.size.toLong())
        exchange.responseBody.use { it.write(bytes) }
    }
    exchange.close()
}

private fun listRemoteDocuments(exchange: HttpExchange) {
    val dir = File(REMOTE_DOCUMENTS_DIR)
    if (!dir.exists()) dir.mkdirs()
    val files = dir.listFiles()?.filter { it.isFile && it.name.endsWith(".rc") }?.map { it.name } ?: emptyList()
    val body = files.joinToString("\n") { "/remote/documents/$it" }.ifEmpty { "No .rc documents" }
    exchange.responseHeaders.add("Content-Type", "text/plain; charset=UTF-8")
    exchange.sendResponseHeaders(200, body.toByteArray(Charsets.UTF_8).size.toLong())
    exchange.responseBody.use { it.write(body.toByteArray(Charsets.UTF_8)) }
    exchange.close()
}

private fun remoteInfoPage(): String = """
<!DOCTYPE html>
<html><head><meta charset="utf-8"><title>Remote</title></head>
<body style="font-family:system-ui;max-width:480px;margin:2rem auto;padding:0 1rem">
  <h1>Remote</h1>
  <p>Pick a <b>button color</b>, then tap <b>Save</b>. The app will render the button with this color (tap <b>Doc</b> to open, then <b>Refresh</b> after saving a new color).</p>
  <form id="f" style="display:flex;align-items:center;gap:1rem;margin:1rem 0">
    <input type="color" id="color" name="color" value="$remoteColor" style="width:64px;height:64px;border:2px solid #ccc;cursor:pointer">
    <button type="submit" style="padding:0.5rem 1.5rem;background:#6200EE;color:white;border:none;border-radius:8px;cursor:pointer">Upload</button>
    <button type="button" id="saveBtn" style="padding:0.5rem 1.5rem;background:#03DAC6;color:#000;border:none;border-radius:8px;cursor:pointer">Save</button>
  </form>
  <p style="color:#666;font-size:0.9rem">Current: <code id="cur">$remoteColor</code> · <b>Save</b> builds the button document with this color. In the app: open with <b>Doc</b>, then tap <b>Refresh</b> to fetch and render the latest.</p>
  <script>
    document.getElementById('f').onsubmit=function(e){e.preventDefault();var h=document.getElementById('color').value;
    fetch('/remote/color',{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded'},body:'color='+encodeURIComponent(h)}).then(function(){document.getElementById('cur').textContent=h});};
    document.getElementById('saveBtn').onclick=function(){var h=document.getElementById('color').value; fetch('/remote/color',{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded'},body:'color='+encodeURIComponent(h)}).then(function(){document.getElementById('cur').textContent=h; alert('Color saved. GET /remote/document now returns the button with this color. In the app tap Refresh.');});};
  </script>
  <hr style="margin:2rem 0">
  <p><a href="/remote/document">/remote/document</a> · <a href="/remote/documents">/remote/documents</a></p>
</body></html>
""".trimIndent()
