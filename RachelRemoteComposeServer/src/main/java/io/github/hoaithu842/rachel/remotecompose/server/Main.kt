package io.github.hoaithu842.rachel.remotecompose.server

import com.sun.net.httpserver.HttpServer
import java.io.File
import java.io.OutputStream
import java.net.InetSocketAddress
import java.nio.file.Files
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors

private const val PORT = 8081
private const val REMOTE_DOCUMENTS_DIR = "documents"

@Volatile
private var remoteColor: String = "#6200EE"

/** "writer" = ButtonDocumentGenerator with remoteColor; anything else = filename in documents/ */
@Volatile
private var activeSource: String = "writer"

private val sseClients = CopyOnWriteArrayList<OutputStream>()

private fun notifyClients() {
    val msg = "data: update\n\n".toByteArray()
    sseClients.forEach { out ->
        try {
            out.write(msg)
            out.flush()
        } catch (_: Exception) {
            sseClients.remove(out)
        }
    }
}

fun main() {
    val server = HttpServer.create(InetSocketAddress("0.0.0.0", PORT), 0)
    server.executor = Executors.newCachedThreadPool()

    // ── GET /remote/document — the single endpoint the app fetches ──
    server.createContext("/remote/document") { exchange ->
        if (exchange.requestMethod != "GET") {
            exchange.sendResponseHeaders(405, -1)
            exchange.close()
            return@createContext
        }
        val bytes: ByteArray = if (activeSource == "writer") {
            try {
                ButtonDocumentGenerator.buildButtonDocument(remoteColor, "Click Me")
            } catch (e: Exception) {
                e.printStackTrace(); ByteArray(0)
            }
        } else {
            val file = File(REMOTE_DOCUMENTS_DIR, activeSource).canonicalFile
            val docsDir = File(REMOTE_DOCUMENTS_DIR).canonicalPath
            if (file.canonicalPath.startsWith(docsDir) && file.isFile) {
                Files.readAllBytes(file.toPath())
            } else {
                ByteArray(0)
            }
        }
        if (bytes.isEmpty()) {
            val msg = "Document generation failed or file not found."
            val b = msg.toByteArray(Charsets.UTF_8)
            exchange.responseHeaders.add("Content-Type", "text/plain; charset=UTF-8")
            exchange.sendResponseHeaders(500, b.size.toLong())
            exchange.responseBody.use { it.write(b) }
        } else {
            exchange.responseHeaders.add("Content-Type", "application/octet-stream")
            exchange.sendResponseHeaders(200, bytes.size.toLong())
            exchange.responseBody.use { it.write(bytes) }
        }
        exchange.close()
    }

    // ── POST /remote/color — web sets the writer color ──
    server.createContext("/remote/color") { exchange ->
        if (exchange.requestMethod != "POST") {
            exchange.sendResponseHeaders(405, -1)
            exchange.close()
            return@createContext
        }
        val body = exchange.requestBody.bufferedReader().readText()
        val color = body.trim().takeIf { it.matches(Regex("^#[0-9A-Fa-f]{6}$")) }
            ?: body.split("&")
                .mapNotNull {
                    it.split("=", limit = 2).let { p ->
                        if (p.size == 2) p[0].trim() to java.net.URLDecoder.decode(
                            p[1],
                            "UTF-8"
                        ) else null
                    }
                }
                .toMap()["color"]
                ?.takeIf { it.matches(Regex("^#[0-9A-Fa-f]{6}$")) }
        if (color != null) {
            remoteColor = color
            notifyClients()
        }
        exchange.sendResponseHeaders(200, -1)
        exchange.responseBody.use { }
        exchange.close()
    }

    // ── POST /remote/source — web switches between writer / file ──
    server.createContext("/remote/source") { exchange ->
        if (exchange.requestMethod != "POST") {
            exchange.sendResponseHeaders(405, -1)
            exchange.close()
            return@createContext
        }
        val body = exchange.requestBody.bufferedReader().readText().trim()
        activeSource = body.ifEmpty { "writer" }
        println("Active source → $activeSource")
        notifyClients()
        exchange.sendResponseHeaders(200, -1)
        exchange.responseBody.use { }
        exchange.close()
    }

    // ── GET /remote/events — SSE stream, pushes "update" when source/color changes ──
    server.createContext("/remote/events") { exchange ->
        if (exchange.requestMethod != "GET") {
            exchange.sendResponseHeaders(405, -1)
            exchange.close()
            return@createContext
        }
        exchange.responseHeaders.add("Content-Type", "text/event-stream")
        exchange.responseHeaders.add("Cache-Control", "no-cache")
        exchange.responseHeaders.add("Connection", "keep-alive")
        exchange.sendResponseHeaders(200, 0)
        val out = exchange.responseBody
        sseClients.add(out)
        println("SSE client connected (${sseClients.size} total)")
    }

    // ── GET /remote — web UI ──
    server.createContext("/remote") { exchange ->
        if (exchange.requestURI.path != "/remote" && exchange.requestURI.path != "/remote/") {
            exchange.sendResponseHeaders(404, -1)
            exchange.close()
            return@createContext
        }
        val body = webPage()
        exchange.responseHeaders.add("Content-Type", "text/html; charset=UTF-8")
        val bytes = body.toByteArray(Charsets.UTF_8)
        exchange.sendResponseHeaders(200, bytes.size.toLong())
        exchange.responseBody.use { it.write(bytes) }
        exchange.close()
    }

    // ── / → redirect to /remote ──
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
    println("  GET  /remote/document  — serves active document to app")
    println("  POST /remote/color     — set writer color (web)")
    println("  POST /remote/source    — switch writer / file (web)")
}

// ── Web page ──

private fun webPage(): String {
    val dir = File(REMOTE_DOCUMENTS_DIR)
    if (!dir.exists()) dir.mkdirs()
    val rcFiles = dir.listFiles()
        ?.filter { it.isFile && it.name.endsWith(".rc") }
        ?.map { it.name }
        ?.sorted()
        ?: emptyList()

    val fileButtons = if (rcFiles.isEmpty()) {
        "<p style='color:#999'>No .rc files in documents/ yet.</p>"
    } else {
        rcFiles.joinToString("\n") { name ->
            val sel = if (activeSource == name) " style='background:#03DAC6;color:#000'" else ""
            "<button class='file-btn' data-name='$name'$sel>$name</button>"
        }
    }

    return """
<!DOCTYPE html>
<html><head><meta charset="utf-8"><title>Remote</title>
<style>
  body{font-family:system-ui;max-width:520px;margin:2rem auto;padding:0 1rem}
  h1{margin-bottom:.25rem} h2{margin-top:1.5rem;margin-bottom:.5rem}
  .card{border:1px solid #ddd;border-radius:12px;padding:1rem;margin-bottom:1rem}
  .active-card{border-color:#03DAC6;background:#f0fdfb}
  button{padding:.5rem 1.2rem;border:none;border-radius:8px;cursor:pointer;margin:.25rem}
  .primary{background:#6200EE;color:white}
  .file-btn{background:#eee;color:#333;display:block;width:100%;text-align:left;margin:.4rem 0}
  .file-btn:hover{background:#ddd}
  .status{font-size:.85rem;color:#666;margin-top:.5rem}
  code{background:#f5f5f5;padding:2px 6px;border-radius:4px}
</style></head>
<body>
  <h1>Remote Compose Server</h1>
  <p class="status">Active source: <code id="src">${activeSource}</code></p>

  <h2>Option 1 — Color Picker (Writer)</h2>
  <div class="card${if (activeSource == "writer") " active-card" else ""}" id="writerCard">
    <p>Pick a color → generates button via <code>RemoteComposeWriter</code></p>
    <div style="display:flex;align-items:center;gap:1rem">
      <input type="color" id="color" value="$remoteColor" style="width:56px;height:56px;border:2px solid #ccc;cursor:pointer">
      <button class="primary" id="useWriter">Use this color</button>
    </div>
    <p class="status">Color: <code id="cur">$remoteColor</code></p>
  </div>

  <h2>Option 2 — File from documents/</h2>
  <div class="card${if (activeSource != "writer") " active-card" else ""}" id="fileCard">
    <p>Serve a <code>.rc</code> file generated by <b>app-controller</b>.</p>
    $fileButtons
    <button class="primary" onclick="location.reload()" style="margin-top:.5rem;background:#888">Refresh list</button>
  </div>

  <hr>
  <p class="status">The app fetches <a href="/remote/document">/remote/document</a> which returns whichever source is active.</p>

<script>
document.getElementById('useWriter').onclick=function(){
  var h=document.getElementById('color').value;
  fetch('/remote/color',{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded'},body:'color='+encodeURIComponent(h)})
  .then(function(){return fetch('/remote/source',{method:'POST',body:'writer'})})
  .then(function(){
    document.getElementById('cur').textContent=h;
    document.getElementById('src').textContent='writer';
    document.getElementById('writerCard').classList.add('active-card');
    document.getElementById('fileCard').classList.remove('active-card');
  });
};
document.querySelectorAll('.file-btn').forEach(function(btn){
  btn.onclick=function(){
    var name=this.getAttribute('data-name');
    fetch('/remote/source',{method:'POST',body:name})
    .then(function(){
      document.getElementById('src').textContent=name;
      document.getElementById('fileCard').classList.add('active-card');
      document.getElementById('writerCard').classList.remove('active-card');
      document.querySelectorAll('.file-btn').forEach(function(b){b.style.background='#eee';b.style.color='#333'});
      btn.style.background='#03DAC6';btn.style.color='#000';
    });
  };
});
</script>
</body></html>
""".trimIndent()
}
