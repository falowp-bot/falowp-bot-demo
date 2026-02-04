package com.blr19c.falowp.demo.plugins.config

import com.blr19c.falowp.bot.system.plugin.Plugin
import com.blr19c.falowp.bot.system.plugin.message.message
import com.blr19c.falowp.bot.system.pluginConfigProperty
import com.blr19c.falowp.bot.system.systemConfigProperty

@Plugin(name = "配置演示", desc = "配置演示")
class Config {

    private val config = message(Regex("获取配置")) {
        val config1 = pluginConfigProperty("config1")
        val nickname = systemConfigProperty("nickname")
        this.sendReply(nickname + config1)
    }

    init {
        config.register()
    }

}
