package com.blr19c.falowp.demo.plugins.web

import com.blr19c.falowp.bot.system.api.SendMessage
import com.blr19c.falowp.bot.system.expand.encodeToBase64String
import com.blr19c.falowp.bot.system.plugin.Plugin
import com.blr19c.falowp.bot.system.plugin.message.message
import com.blr19c.falowp.bot.system.web.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Plugin(name = "Web演示", desc = "WebClient/WebDriver/WebServer演示")
class Web {

    private val webGet = message(Regex("网页GET\\s+(.+)")) { (rawUrl) ->
        val url = normalizeUrl(rawUrl)
        val response = webclient().get(url)
        val jsonText = runCatching { response.bodyAsJsonNode().toPrettyString() }.getOrNull()
        val body = jsonText ?: response.bodyAsText()
        this.sendReply(
            """
            status: ${response.status}
            url: $url
            body:
            ${body.take(1200)}
            """.trimIndent()
        )
    }

    private val webRedirect = message(Regex("网页重定向\\s+(.+)")) { (rawUrl) ->
        val url = normalizeUrl(rawUrl)
        val redirectUrl = webclient().urlToRedirectUrl(url)
        this.sendReply(redirectUrl ?: "无重定向: $url")
    }

    private val webTitle = message(Regex("网页标题\\s+(.+)")) { (rawUrl) ->
        val url = normalizeUrl(rawUrl)
        val title = withContext(Dispatchers.IO) {
            commonWebdriverContextPage {
                this.navigate(url)
                this.title()
            }
        }
        this.sendReply("title: $title")
    }

    private val webScreenshot = message(Regex("网页截图\\s+(.+)")) { (rawUrl) ->
        val url = normalizeUrl(rawUrl)
        val imageBase64 = withContext(Dispatchers.IO) {
            commonWebdriverContextPage {
                this.navigate(url)
                this.screenshot().encodeToBase64String()
            }
        }
        this.sendReply(SendMessage.builder().text("网页截图: $url").image(imageBase64).build())
    }

    private val htmlScreenshot = message(Regex("HTML截图\\s+(.+)")) { (text) ->
        val html = """
               <h2 style="margin:0 0 12px 0;">HTML截图演示</h2>
        """.trimIndent()
        val imageBase64 = htmlToImageBase64(html, "body")
        this.sendReply(SendMessage.builder().text("HTML转图片").image(imageBase64).build())
    }

    private val webRouteHelp = message(Regex("Web路由演示")) {
        this.sendReply("访问: http://127.0.0.1:8041/demo/web/ping")
    }

    init {
        webGet.register()
        webRedirect.register()
        webTitle.register()
        webScreenshot.register()
        htmlScreenshot.register()
        webRouteHelp.register()

        WebServer.registerRoute {
            get("/demo/web/ping") {
                call.respond(
                    mapOf(
                        "ok" to true,
                        "message" to "web route ready",
                        "timestamp" to System.currentTimeMillis()
                    )
                )
            }
        }
    }

    private fun normalizeUrl(raw: String): String {
        return if (raw.startsWith("http:")) raw else "https://$raw"
    }
}
