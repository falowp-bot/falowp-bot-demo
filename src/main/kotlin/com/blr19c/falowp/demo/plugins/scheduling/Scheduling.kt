package com.blr19c.falowp.demo.plugins.scheduling

import com.blr19c.falowp.bot.system.api.ReceiveMessage
import com.blr19c.falowp.bot.system.plugin.Plugin
import com.blr19c.falowp.bot.system.plugin.event.eventListener
import com.blr19c.falowp.bot.system.plugin.task.cronScheduling
import com.blr19c.falowp.bot.system.plugin.task.periodicScheduling
import kotlin.time.Duration.Companion.seconds

@Plugin(name = "定时演示")
class Scheduling {


    private val cron = cronScheduling("*/5 * * * * ?") {
        //println("5秒秒钟执行一次")
        //定时任务中发送消息不能使用sendReply,需要手动指定来源,或者发送给所有
        //this.sendGroup(sourceId = "")
        //this.sendPrivate(sourceId = "")
        //this.sendAllGroup()
        //使用定时触发事件
        val schedulingEvent = SchedulingEvent(
            "使用定时触发事件",
            ReceiveMessage.User.empty(),
            ReceiveMessage.Source.system()
        )
        this.publishEvent(schedulingEvent)
    }

    private val periodic = periodicScheduling(10.seconds) {
        //println("10秒钟执行一次")
    }

    private val eventListener = eventListener<SchedulingEvent> { event ->
        println(event)
    }

    data class SchedulingEvent(
        val text: String,
        override val actor: ReceiveMessage.User,
        override val source: ReceiveMessage.Source
    ) : Plugin.Listener.Event

    init {
        cron.register()
        periodic.register()
        eventListener.register()
    }
}
