package com.blr19c.falowp.demo.plugins.help

import com.blr19c.falowp.bot.system.listener.events.HelpEvent
import com.blr19c.falowp.bot.system.plugin.Plugin
import com.blr19c.falowp.bot.system.plugin.message.MessageMatch
import com.blr19c.falowp.bot.system.plugin.message.message

/**
 * 帮助
 */
@Plugin(name = "帮助", desc = "使用系统帮助组件提供组件信息", hidden = true)
class Help {

    private val help = message(MessageMatch(Regex("帮助"), atMe = true)) {
        this.publishEvent(HelpEvent(this.receiveMessage.source, this.receiveMessage.sender))
    }

    private val helpItem = message(MessageMatch(Regex("帮助\\s?(.+)"), atMe = true)) { (pluginName) ->
        this.publishEvent(
            HelpEvent(
                source = this.receiveMessage.source,
                actor = this.receiveMessage.sender,
                pluginName = pluginName.trim()
            )
        )
    }

    init {
        help.register()
        helpItem.register()
    }
}