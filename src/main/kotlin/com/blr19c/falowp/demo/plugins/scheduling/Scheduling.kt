package com.blr19c.falowp.demo.plugins.scheduling

import com.blr19c.falowp.bot.system.api.ReceiveMessage
import com.blr19c.falowp.bot.system.api.SendMessage
import com.blr19c.falowp.bot.system.plugin.Plugin
import com.blr19c.falowp.bot.system.plugin.event.eventListener
import com.blr19c.falowp.bot.system.plugin.task.applicationInitScheduling
import com.blr19c.falowp.bot.system.plugin.task.cronScheduling
import com.blr19c.falowp.bot.system.plugin.task.periodicScheduling
import com.blr19c.falowp.bot.system.pluginConfigListProperty
import kotlin.time.Duration.Companion.seconds

@Plugin(name = "定时演示")
class Scheduling {

    private val notifyGroups by lazy {
        pluginConfigListProperty("notifyGroups") { emptyList() }
    }

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

    private val appInit = applicationInitScheduling {
        println("应用启动完成,通知群配置:$notifyGroups")
    }

    private val periodicNotify = periodicScheduling(
        period = 30.seconds,
        initialDelay = 5.seconds,
        fixedRate = true,
        useGreeting = false
    ) {
        if (notifyGroups.isEmpty()) return@periodicScheduling
        notifyGroups.forEach { groupId ->
            this.sendGroup(SendMessage.builder("固定速率任务通知").build(), sourceId = groupId)
        }
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
        appInit.register()
        periodicNotify.register()
        eventListener.register()
    }
}
