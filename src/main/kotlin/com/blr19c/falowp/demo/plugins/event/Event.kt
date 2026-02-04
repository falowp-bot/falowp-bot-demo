package com.blr19c.falowp.demo.plugins.event

import com.blr19c.falowp.bot.system.api.SendMessage
import com.blr19c.falowp.bot.system.listener.events.WithdrawMessageEvent
import com.blr19c.falowp.bot.system.plugin.Plugin
import com.blr19c.falowp.bot.system.plugin.event.eventListener
import com.blr19c.falowp.bot.system.plugin.event.nudgeMe

@Plugin(name = "事件演示")
class Event {

    private val nudgeEvent = nudgeMe { e ->
        println("被戳一戳:$e")
        //戳回去
        this.sendReply(SendMessage.builder().nudge(e).build())
    }

    private val withdrawMessageEvent = eventListener<WithdrawMessageEvent> { e ->
        println("撤回了消息:${e}")
    }

    init {
        nudgeEvent.register()
        withdrawMessageEvent.register()
    }
}