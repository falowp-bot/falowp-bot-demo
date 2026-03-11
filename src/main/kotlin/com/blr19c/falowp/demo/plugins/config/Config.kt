package com.blr19c.falowp.demo.plugins.config

import com.blr19c.falowp.bot.system.adapterConfigProperty
import com.blr19c.falowp.bot.system.plugin.Plugin
import com.blr19c.falowp.bot.system.plugin.message.message
import com.blr19c.falowp.bot.system.pluginConfigListProperty
import com.blr19c.falowp.bot.system.pluginConfigProperty
import com.blr19c.falowp.bot.system.systemConfigListProperty
import com.blr19c.falowp.bot.system.systemConfigProperty

@Plugin(name = "配置演示", desc = "配置演示")
class Config {

    private val config = message(Regex("获取配置")) {
        val config1 = pluginConfigProperty("config1") { "默认config1" }
        val config2 = pluginConfigProperty("config2") { "默认config2" }
        val pluginList = pluginConfigListProperty("list") { emptyList() }
        val nickname = systemConfigProperty("nickname")
        val adminList = systemConfigListProperty("administrator") { emptyList() }
        val useHttp = adapterConfigProperty("nc.useHttp") { "false" }
        this.sendReply(
            """
            system.nickname: $nickname
            system.administrator: ${adminList.joinToString(",")}
            adapter.nc.useHttp: $useHttp
            plugin.config1: $config1
            plugin.config2(default): $config2
            plugin.list: ${pluginList.joinToString(",")}
            """.trimIndent()
        )
    }

    init {
        config.register()
    }

}
