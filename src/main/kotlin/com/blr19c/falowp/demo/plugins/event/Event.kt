package com.blr19c.falowp.demo.plugins.event

import com.blr19c.falowp.bot.system.api.SendMessage
import com.blr19c.falowp.bot.system.listener.events.GreetingEvent
import com.blr19c.falowp.bot.system.listener.events.GroupDecreaseEvent
import com.blr19c.falowp.bot.system.listener.events.GroupIncreaseEvent
import com.blr19c.falowp.bot.system.listener.events.RequestAddFriendEvent
import com.blr19c.falowp.bot.system.listener.events.RequestJoinGroupEvent
import com.blr19c.falowp.bot.system.listener.events.SendMessageEvent
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

    private val groupIncreaseEvent = eventListener<GroupIncreaseEvent> { e ->
        this.sendGroup(
            SendMessage.builder().at(e.user.id).text("欢迎 ${e.user.nickname.ifBlank { e.user.id }} 加入群聊").build(),
            sourceId = e.source.id
        )
    }

    private val groupDecreaseEvent = eventListener<GroupDecreaseEvent> { e ->
        println("有人退群了: $e")
    }

    private val requestJoinGroupEvent = eventListener<RequestJoinGroupEvent> { e ->
        println("收到入群申请: $e")
    }

    private val requestAddFriendEvent = eventListener<RequestAddFriendEvent> { e ->
        println("收到加好友申请: $e")
    }

    private val greetingEvent = eventListener<GreetingEvent> { e ->
        println("收到问候事件: $e")
    }

    private val sendMessageEvent = eventListener<SendMessageEvent> { e ->
        println("机器人发送消息事件: size=${e.sendMessage.size}, reference=${e.reference}, forward=${e.forward}")
    }

    init {
        nudgeEvent.register()
        withdrawMessageEvent.register()
        groupIncreaseEvent.register()
        groupDecreaseEvent.register()
        requestJoinGroupEvent.register()
        requestAddFriendEvent.register()
        greetingEvent.register()
        sendMessageEvent.register()
    }
}
