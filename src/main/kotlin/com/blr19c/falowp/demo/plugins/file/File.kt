package com.blr19c.falowp.demo.plugins.file

import com.blr19c.falowp.bot.system.plugin.Plugin
import com.blr19c.falowp.bot.system.plugin.message.message
import com.blr19c.falowp.bot.system.readPluginResource

@Plugin(name = "文件演示", desc = "文件演示")
class File {

    private val file = message(Regex("获取文件")) {
        val text = readPluginResource("file.txt") {
            it.readBytes().decodeToString()
        }
        this.sendReply(text)
    }

    init {
        file.register()
    }
}